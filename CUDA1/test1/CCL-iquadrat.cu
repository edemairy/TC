#include <string.h>
#include <iostream>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <vector>
#include <stdio.h>
#include <vector>
#include <iostream>
#include <stdlib.h>
#include <sys/time.h>
#include <climits>
#include <QImage>
#ifndef TIMER_H
#define TIMER_H


class Timer {
  
public:
    
    static double now() {
        timeval now;
        gettimeofday(&now, 0);
        return now.tv_sec + now.tv_usec/1000000.0;
    }
    
    static long nowMilisecs() {
        timeval now;
        gettimeofday(&now, 0);
        return (long) (now.tv_sec*1000 + now.tv_usec/1000.0);
    }
    
    Timer() {
        elapsed = 0;
        running = false;
    }
    
    void start() {
        running = true;
        gettimeofday(&started, 0);
    }
    
    void restart() {
      running = true;
      elapsed = 0;
      gettimeofday(&started, 0);
    }
    
    double getTimeElapsed() {
           return elapsed + ( (running) ? timeSinceStart() : 0 ); 
    }
    
    double stopAndAddTime() {
       double dt = timeSinceStart();
       if (dt <0) {
           //exit(9);
       }
       elapsed += dt;
       running = false;
       return elapsed;
    }
    
    void reset() {
        elapsed = 0;
        running = false;
    }

private:
    
    double timeSinceStart() {
        timeval now;
        gettimeofday(&now, 0);
        return (now.tv_sec - started.tv_sec) + (now.tv_usec - started.tv_usec)/1000000.0;
    }

    timeval started;    
    volatile double  elapsed;
    volatile bool    running;    
    
};

#endif

#define PRINT_TIMINGS
#define FIND_VARIANT1

using std::vector;

enum {
    NW = 1, N = 4, NE = 5,
    W  = 0
};

inline void resize_non_initialized(vector<int>& v, int size)
{
    v.reserve(size);
    ((int**)&v)[1] = ((int**)&v)[0]+size;
}


class CCL {


    unsigned int v[4] __attribute__ ((aligned(16)));    
    
    vector<int>* m_result;
    vector<int>* m_img;
    int m_W;
    int m_H;
    int m_conn;
    int m_thresh;
    int m_thresh_p1;    
    bool m_diag;
    Timer t;
    
public:
     inline vector<int> cuda_ccl(vector<int>& img, int W, int conn, int threshold) {
     
        vector<int> result_vec;    
                
#ifdef PRINT_TIMINGS        
        printf("start\n");
#endif        
//        printf("gcc version %d.%d.%d\n",__GNUC__,__GNUC_MINOR__,__GNUC_PATCHLEVEL__);
        
        t.start();
        m_W = W;
        m_H = img.size() / W;
        m_conn = conn;
        m_thresh = threshold;
        m_thresh_p1 = threshold+1;
        m_img = &img;
        m_diag = (m_conn == 8);                

        m_result = &img;    
//        m_result = new vector<int>(m_W*m_H);
//    m_result = &result_vec;
//        resize_non_initialized(result_vec, m_W*m_H);
        
        internal_cuda_ccl();

#ifdef PRINT_TIMINGS
        printf("t total = %f\n", t.getTimeElapsed());
        fprintf(stderr, "done in %f, (stderr)\n", t.getTimeElapsed());
        
        fflush(stdout);
        fflush(stdin);
#endif        

        result_vec.swap(*m_result);
        return result_vec;
    }
    
    void internal_cuda_ccl();

private:


    
    void cuda_neighbour_process();    

    void forward_pass();
    
    void final_pass();
    
    inline void compute_min(int src, int nv1);
    inline void compute_min(int src, int nv1, int nv2);    
    inline void compute_min(int src, int nv1, int nv2, int nv3);    
    inline void compute_min(int src, int nv1, int nv2, int nv3, int nv4);        
    
    inline int get_neigh(int idx, int off, int dir);
    
    inline int img(int x, int y);
    
  inline int& src_img(int x, int y) {
        return m_img->operator[](y*m_W + x);
    }
    
    inline int& result_img(int x, int y) {
        return m_result->operator[](y*m_W + x);
    }
    
    inline int& result_img(int off) {
        return m_result->operator[](off);
    }
    
    inline bool is_conn(int x1, int y1, int dir);
    
    inline int is_conn(int* p1, int* p2) {

#ifdef HAVE_INTRINSICS    
      __m128i i1 = _mm_set_epi32(0,0,0,*p1);
        __m128i i2 = _mm_set_epi32(0,0,0,*p2);
        
        __m128i d  = _mm_sad_epu8(i1,i2);
        __m128i t  = _mm_set_epi32(0,0,0,m_thresh_p1);
        __m128i c  = _mm_cmplt_epi32(d,t);        
        return _mm_movemask_epi8(c) & 1;
#else 

    // PRECONDITION m_thresh_p1 in %xmm7

    int result;
     
    __asm__ __volatile__(
        "movd    (%[v1]), %%xmm1 \n\t"
            "movd    (%[v2]), %%xmm0 \n\t"
            "psadbw    %%xmm0, %%xmm1 \n\t"
      "movdqa %%xmm7, %%xmm0 \n\t"
            "pcmpgtd    %%xmm1, %%xmm0 \n\t"            
            "pmovmskb    %%xmm0, %0 \n\t"
            "andl    $1, %0"
          : "=r"(result)
            : [v1] "r" (p1), [v2] "r" (p2), [t] "r" (&m_thresh_p1)
            : 
        );
        
        return result;

#endif        
    }
    
    
    inline int dist(int v1, int v2);
    
    inline void unite(int g1, int g2);
    
    inline int find0(int v);
    
    inline int find1(int v);
    
    inline void findAndUnite(int v1, int v2);    
    
    inline void findAndUniteIfConnected(int v1, int v2);    
    
    int blue(int v);
    
    int green(int v);    
    
    int red(int v);        
    
};



#ifdef FIND_VARIANT1
int CCL::find1(int x) {
    int c = x;
    int p = (*m_result)[c];
    int gp = (*m_result)[p];
    if (p == gp) return p;
    
    while(gp != p) {
        (*m_result)[p] = gp;
        c = p;
        p = gp;
        gp = (*m_result)[gp];
    }
    return p;
}
#endif

#ifdef FIND_VARIANT2
int CCL::find1(int v) {
  int c  = v;
    int p  = (*m_result)[c];
    int gp = (*m_result)[p];
    if (p == gp) return p;
    
  do {
      (*m_result)[c] = gp;
        c = p;
        p = gp;
        gp = (*m_result)[gp];
    } while (p != gp);
  (*m_result)[c] = gp;    
  return p;
}
#endif

int CCL::find0(int v) {
  int c = (*m_result)[v];
    int p = (*m_result)[c];
    if (c == p) return c;
  do {
        c = p;
        p = (*m_result)[c];
    } while(c != p);
    return c;
}


void CCL::findAndUnite(int v1, int v2) {
    int g1 = find0(v1);
    int g2 = find0(v2);
    
    if (g1 < g2) {
      unite(g1, g2);
    } else if (g2 < g1) {
      unite(g2, g1);
    }
}

void CCL::findAndUniteIfConnected(int v1, int v2) {
  if (!is_conn(&(*m_img)[v1], &(*m_img)[v2])) return;
    findAndUnite(v1, v2);
}
    
// g1 <= g2!
void CCL::unite(int g1, int g2) {
    (*m_result)[g2] = g1;
} 

//#define HAVE_INTRINSICS


#ifdef HAVE_INTRINSICS

void debug(__m128i v) {
    unsigned int f[4];
    _mm_store_si128((__m128i*)&f[0], v);
    printf("<%u, %u, %u, %u>", f[0], f[1], f[2], f[3]);
  }

#endif

using std::vector;
using std::cout;
using std::endl;


inline unsigned int min2(unsigned int a, unsigned int b) {
    return a < b ? a : b;
}

unsigned int getCpuId() {

  unsigned int a,b,c,d;    
    unsigned int v = 1;
    __asm__ __volatile__ ("cpuid" : "=a" (a), "=b" (b), "=c" (c), "=d" (d) : "a" (v));
    
    std::cout << a << "/" << b << "/" << c << "/" << d << std::endl;
    return c;
}

#ifdef HAVE_SSE4
    #define PMIN10 "pminud  %%xmm1, %%xmm0 \n\t"
#else
    #define PMIN10  \
        "movdqa   %%xmm1, %%xmm4 \n\t" \
        "movdqa   %%xmm1, %%xmm5 \n\t" \
        "pcmpgtd  %%xmm0, %%xmm5 \n\t" \
        "pand     %%xmm5, %%xmm0 \n\t" \
        "pandn    %%xmm4, %%xmm5 \n\t" \
        "por      %%xmm5, %%xmm0 \n\t" 
#endif

#ifdef HAVE_SSE4
    #define PMIN03 "pminud  %%xmm0, %%xmm3 \n\t"
#else
    #define PMIN03  \
        "movdqa   %%xmm0, %%xmm4 \n\t" \
        "movdqa   %%xmm0, %%xmm5 \n\t" \
        "pcmpgtd  %%xmm3, %%xmm5 \n\t" \
        "pand     %%xmm5, %%xmm3 \n\t" \
        "pandn    %%xmm4, %%xmm5 \n\t" \
        "por      %%xmm5, %%xmm3 \n\t" 
#endif

inline void min4(unsigned int* v) {

//    std::cout << "min4 " << v[0] << "," << v[1] << "," << v[2] << "," << v[3] << std::endl;
#ifdef ASM_MIN4
  __asm__ __volatile__ (
      
        "movdqa (%[pv]), %%xmm0 \n\t"
        "pshufd $14, %%xmm0, %%xmm1\n\t"
        PMIN10
        "pshufd $1, %%xmm0, %%xmm1\n\t"
        PMIN10
        "movdqa %%xmm0, (%[pv]) \n\t"
        :
        : [pv] "r" (v)
        : "memory"
    );
    
//    std::cout << "->  " << v[0] << "," << v[1] << "," << v[2] << "," << v[3] << std::endl;
    
//    std::cout << "min=" << v[0] << endl;
#else    
    int ab = min2(v[0],v[1]);
    int cd = min2(v[2],v[3]);
    v[0] = min2(ab, cd);
#endif    
}

void test_min4(int a, int b, int c, int d) {

    unsigned int v[4] __attribute__ ((aligned(16)));    
    v[0] = a;
    v[1] = b;
    v[2] = c;
    v[3] = d;    
    min4(v);        
    
    cout << "->" << v[0] << endl;
    
}

inline void min(int count, unsigned int* v) {
    if (count == 1) return;
    if (count == 2) {
        v[0] = min2(v[0], v[1]);
    } else if (count == 3) {
        v[0] = min2(min2(v[0], v[1]), v[2]);
    } else if (count == 4) {
        min4(v);
    } else {
        exit(6);
    }
}



void CCL::compute_min(int src, int nv1) {
  int g1 = find1(nv1);
    unite(g1, src);
}

void CCL::compute_min(int src, int nv1, int nv2) {
  int g1 = find1(nv1);
    int g2 = find1(nv2);
    int gmin = min2(g1, g2);
    unite(gmin, g1);
    unite(gmin, g2);
    unite(gmin, src);
}

void CCL::compute_min(int src, int nv1, int nv2, int nv3) {
  int g1 = find1(nv1);
    int g2 = find1(nv2);
    int g3 = find1(nv3);    
    int gmin = min2(min2(g1, g2), g3);
    unite(gmin, g1);
    unite(gmin, g2);
    unite(gmin, g3);    
    unite(gmin, src);
}

void CCL::compute_min(int src_v, int nv1, int nv2, int nv3, int nv4) {
  int g1 = find1(nv1);
    int g2 = find1(nv2);
    int g3 = find1(nv3);    
    int g4 = find1(nv4);    
    int gmin = min2(min2(g1, g2), min2(g3,g4));
    unite(gmin, g1);
    unite(gmin, g2);
  unite(gmin, g3);
  unite(gmin, g4);    
    unite(gmin, src_v);
}

void CCL::internal_cuda_ccl() {
    
      cout << "width=" << m_W << " height=" << m_H << endl;
        cout << "thresh=" << m_thresh << " conn=" << m_conn << endl;
        
        cout << "cpuid=" << getCpuId() << endl;
    
#ifndef HAVE_INTRINSICS
      cout << "using asm version" << endl;
   __asm__ __volatile__ (
        "movd    (%[t]), %%xmm7 \n\t"
            "pshufd $68, %%xmm7,  %%xmm7 \n\t"
            :
            : [t] "r" (&m_thresh_p1)
            :
     );
#endif         

        cuda_neighbour_process();
                
      Timer t_step;
        
/*        t_step.start();
      forward_pass();
        cout << " time for union     : " <<  t_step.getTimeElapsed() << endl;
*/        
/*        t_step.restart();
        final_pass();
        cout << " time for final find: " <<  t_step.getTimeElapsed() << endl;
*/        
}


void CCL::forward_pass() {
      int size = m_H * m_W;      
        
        int img_nw = -m_W-1;
        int img_w = -1;        

        for(int i = 0; i<size; ++i) {
            int magic = (*m_img)[img_w+1];
                        
                switch(magic) {
                  case 0: {
                    (*m_img)[img_w+1] = img_w+1;
                        break;
                    }                    
                    case 2: {
                        compute_min(img_w+1, img_nw);
                    } break;                    
                    case 16: {
                        compute_min(img_w+1, img_nw+1);
                    } break;
                    case 18: {
                        compute_min(img_w+1, img_nw, img_nw+1);
                    } break;
                    case 32: {
                        compute_min(img_w+1, img_nw+2);
                    } break;
                    case 34: {
                        compute_min(img_w+1, img_nw, img_nw+2);                    
                    } break;
                    case 48: {
                        compute_min(img_w+1, img_nw+1, img_nw+2);
                    } break;
                    case 50: {
                        compute_min(img_w+1, img_nw, img_nw+1, img_nw+2);
                    } break;
                    case 1: {
                        compute_min(img_w+1, img_w);
                    } break;
                    case 3: {
                        compute_min(img_w+1, img_nw, img_w);
                    } break;
                    case 17: {
                        compute_min(img_w+1, img_nw+1, img_w);
                    } break;
                    case 19: {
                        compute_min(img_w+1, img_nw, img_nw+1, img_w);
                    } break;
                    case 33: {
                        compute_min(img_w+1, img_nw+2, img_w);
                    } break;
                    case 35: {
                        compute_min(img_w+1, img_nw, img_nw+2, img_w);                    
                    } break;
                    case 49: {
                        compute_min(img_w+1, img_nw+1, img_nw+2, img_w);                    
                    } break;
                    case 51: {
                        compute_min(img_w+1, img_nw, img_nw+1, img_nw+2, img_w);
                    } break;                
/*                     default:
                    {
                        printf("Oops, got magic number %d with i=%d\n", magic, i);
                    }
 */                     
            }
            
            img_nw++;
            img_w++;
        }
}
    
void CCL::final_pass() {
    int size = m_W * m_H;
    for(int i=0; i<size; ++i) {
        (*m_result)[i] = find0(i);    
    }
}    
        
    int CCL::img(int x, int y) {
        return (*m_img)[y*m_W + x];
    }

    int CCL::dist(int v1, int v2) {
        return abs(red(v1) - red(v2)) + abs(green(v1)- green(v2)) + abs(blue(v1)-blue(v2));    
    }
    
    int CCL::blue(int v) {
        return v & 0xFF; 
    }
    
    int CCL::green(int v) {
        return (v >> 8) & 0xFF;
    }
    
    
    int CCL::red(int v) {
        return (v >> 16) & 0xFF;
    }            

//#define USE_PATH_SHORTENING

// local ccl thread count must be a multiple of BLOCK_SIZE_X and a divider of BLOCK_SIZE_X*BLOCK_SIZE_Y

#define BLOCK_SIZE_X (44)
#define BLOCK_SIZE_Y (44)

#define LOCAL_CCL_THREADS (484)
#define LOCAL_CCL_Y_STEP (LOCAL_CCL_THREADS / BLOCK_SIZE_X)
#define LOCAL_CCL_Y_IT (BLOCK_SIZE_X * BLOCK_SIZE_Y / LOCAL_CCL_THREADS)

#define FINAL_BLOCK_SIZE_X (32)
#define FINAL_BLOCK_SIZE_Y (32)

#define FINAL_PASS_THREADS (512)
#define FINAL_PASS_Y_STEP (FINAL_PASS_THREADS / FINAL_BLOCK_SIZE_X)
#define FINAL_PASS_Y_IT (FINAL_BLOCK_SIZE_X * FINAL_BLOCK_SIZE_Y / FINAL_PASS_THREADS)

__device__ bool is_conn(int v1, int v2, int t) {
/*    int sum = __usad((v1 & 0x0000FF)>>0, (v2 & 0x0000FF)>>0,  0);
        sum = __usad((v1 & 0x00FF00)>>8, (v2 & 0x00FF00)>>8,  sum);
        sum = __usad((v1 & 0xFF0000)>>16,(v2 & 0xFF0000)>>16, sum);    */
            
    int sum  = __usad((v1 & 0xFF0000), (v2 & 0xFF0000), 0) >> 16;
        sum += __usad((v1 & 0x00FF00), (v2 & 0x00FF00), 0) >> 8;                    
        sum  = __usad((v1 & 0x0000FF), (v2 & 0x0000FF), sum);

    return sum <= t;    
}

__device__ int find(int* buf, int x) {
#ifdef USE_PATH_SHORTENING
    int c = x;
    int p = buf[c];
    int gp = buf[p];
    if (p == gp) return p;
    
    while(gp != p) {
        buf[p] = gp;
        c = p;
        p = gp;
        gp = buf[gp];
    }
    return p;
#else    
    
    while (x != buf[x]) {
      x = buf[x];
    }
    return x;
    
#endif
}


__device__ void findAndUnion(int* buf, int g1, int g2) {
    bool done;    
    do {

         g1 = find(buf, g1);
      g2 = find(buf, g2);    
            
      // it should hold that g1 == buf[g1] and g2 == buf[g2] now
    
        if (g1 < g2) {
          int old = atomicMin(&buf[g2], g1);
            done = (old == g2);
            g2 = old;
        } else if (g2 < g1) {
          int old = atomicMin(&buf[g1], g2);
            done = (old == g1);
            g1 = old;
        } else {
          done = true;
        }
        
    } while(!done);
}

void checkCUDAError(const char *msg)
{
    cudaError_t err = cudaGetLastError();
    if( cudaSuccess != err) 
    {
        fprintf(stderr, "Cuda error: %s: %s.\n", msg, cudaGetErrorString( err) );
        exit(74);
    }                         
}

__global__ void join_seams4_cuda(int* d_img, int* d_dst_img, int w, int t, int h) {

    int offset = BLOCK_SIZE_Y * blockIdx.y * w;

    // join x seams

    int ymax = h - blockIdx.y * BLOCK_SIZE_Y;
    if (ymax > BLOCK_SIZE_Y) ymax = BLOCK_SIZE_Y;    

  int n = threadIdx.x * gridDim.x + blockIdx.x;
//  int n = blockIdx.x * blockDim.x + threadIdx.x;

    int x = n / BLOCK_SIZE_Y;
    int y = n % BLOCK_SIZE_Y;

    int idx = offset + y * w + x * BLOCK_SIZE_X;

    if (x > 0 && y < ymax && is_conn(d_img[idx], d_img[idx - 1], t)) {
      findAndUnion(d_dst_img, idx, idx - 1);
    }

    // join y seams

    if (offset != 0) {

        int x = blockIdx.x * BLOCK_SIZE_X + threadIdx.x;
        int idx = offset + x;

        if (x < w && is_conn(d_img[idx], d_img[idx-w], t)) {
          findAndUnion(d_dst_img, idx, idx - w);
        }    

    }
        
}

__global__ void join_seams8_cuda(int* d_img, int* d_dst_img, int w, int t, int h) {
  
    int offset = BLOCK_SIZE_Y * blockIdx.y * w;        
    
    int ymax = h - blockIdx.y * BLOCK_SIZE_Y;
    if (ymax > BLOCK_SIZE_Y) ymax = BLOCK_SIZE_Y;        

    // join x seams    

  int n = threadIdx.x * gridDim.x + blockIdx.x;
//  int n = blockIdx.x * blockDim.x + threadIdx.x;

    int x = n / BLOCK_SIZE_Y;
    int y = n % BLOCK_SIZE_Y;

    int idx = offset + y * w + x * BLOCK_SIZE_X;

    if (x > 0 && y < ymax) {

        if (is_conn(d_img[idx], d_img[idx - 1], t)) {
          findAndUnion(d_dst_img, idx, idx - 1);
        }

        if (y > 0 && is_conn(d_img[idx], d_img[idx - 1 - w], t)) {
          findAndUnion(d_dst_img, idx, idx - 1 - w);
        }

        if (y > 0 && is_conn(d_img[idx - 1], d_img[idx - w], t)) {
          findAndUnion(d_dst_img, idx - 1, idx - w);
        }

    }

    // join y seams

    x = blockIdx.x * BLOCK_SIZE_X + threadIdx.x;

    if (offset != 0 && x < w) {

        int idx = offset + x;

        if (is_conn(d_img[idx], d_img[idx - w], t)) {
          findAndUnion(d_dst_img, idx, idx - w);
        }

        if (x > 0 && is_conn(d_img[idx], d_img[idx - w - 1], t)) {
          findAndUnion(d_dst_img, idx, idx - w - 1);
        }
        if (x > 0 && is_conn(d_img[idx - 1], d_img[idx - w], t)) {
          findAndUnion(d_dst_img, idx - 1, idx - w);
        }

    }    

}


__global__ void final_find_cuda(int* d_dst_img, int w, int h) {
    
    int x = FINAL_BLOCK_SIZE_X * blockIdx.x + threadIdx.x;
    
    for(int i=0; i<FINAL_PASS_Y_IT; ++i) {
      int y = FINAL_BLOCK_SIZE_Y * blockIdx.y + FINAL_PASS_Y_STEP * i + threadIdx.y;
        if (x < w && y < h) {
          int idx = y * w + x;
            d_dst_img[idx] = find(d_dst_img, idx);
        }
    }

}

__global__ void calculate_neighbours4_cuda(int* d_img, int* d_dst_img, int w, int t, int h) {

  extern __shared__ int s_buf[];
    
    int* s_buf2 = &s_buf[BLOCK_SIZE_X * BLOCK_SIZE_Y];
    
    int offset = BLOCK_SIZE_Y * blockIdx.y * w;
  int global_w = BLOCK_SIZE_X * blockIdx.x + threadIdx.x;
    
    int ymax = h - blockIdx.y * BLOCK_SIZE_Y;
    if (ymax > BLOCK_SIZE_Y) ymax = BLOCK_SIZE_Y;        

    for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {
      int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
      s_buf[BLOCK_SIZE_X * y + threadIdx.x] = d_img[offset + w * y + global_w];        
    }
    
  for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {      
      int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
        int idx = BLOCK_SIZE_X * y + threadIdx.x;            
        s_buf2[idx] = idx;
    }
    
    __syncthreads();
    
  for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {

        int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
        int x = threadIdx.x;
        int idx = BLOCK_SIZE_X * y + x;        
        
//        __syncthreads();
              
        // process N
        if (global_w < w && y != 0 && y < ymax && is_conn(s_buf[idx], s_buf[idx - BLOCK_SIZE_X], t)) {
          findAndUnion(s_buf2, idx, idx - BLOCK_SIZE_X);
        }

        __syncthreads();        
        
        // process W
        if (global_w < w && x != 0 && y < ymax && is_conn(s_buf[idx], s_buf[idx -  1], t)) {
          findAndUnion(s_buf2, idx, idx - 1);
        }
        
    }
    
    __syncthreads();
    
    // copy result back
    for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {
      if (global_w >= w) continue;
      int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
        int idx = BLOCK_SIZE_X * y + threadIdx.x;
        
//        int v = s_buf2[idx];
        int v = find(s_buf2, idx);
        
        // convert v to global space
        int v_x = v % BLOCK_SIZE_X;
        int v_y = v / BLOCK_SIZE_X;
        
        d_dst_img[offset + w * y + global_w] = offset + blockIdx.x * BLOCK_SIZE_X + v_y * w + v_x;
    }    
    
}


__global__ void calculate_neighbours8_cuda(int* d_img, int* d_dst_img, int w, int t, int h) {

  extern __shared__ int s_buf[];    
    
    int* s_buf2 = &s_buf[BLOCK_SIZE_Y*BLOCK_SIZE_X];
    
    int offset = BLOCK_SIZE_Y * blockIdx.y * w;    
    int global_w = BLOCK_SIZE_X * blockIdx.x + threadIdx.x;
    
    int ymax = h - blockIdx.y * BLOCK_SIZE_Y;
    if (ymax > BLOCK_SIZE_Y) ymax = BLOCK_SIZE_Y;        

    for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {
      int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
      s_buf[BLOCK_SIZE_X * y + threadIdx.x] = d_img[offset + w * y + global_w];        
    }
    
  for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {      
      int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
        int idx = BLOCK_SIZE_X * y + threadIdx.x;            
        s_buf2[idx] = idx;
    }
    
    __syncthreads();
    
  for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {

        int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
        int x = threadIdx.x;
        
        int idx = BLOCK_SIZE_X * y + x;        
        
//        __syncthreads();
      
        // process NW
        if (global_w < w && x != 0 && y != 0 && y < ymax && is_conn(s_buf[idx], s_buf[idx - BLOCK_SIZE_X - 1], t)) {
          findAndUnion(s_buf2, idx, idx - BLOCK_SIZE_X - 1);
        }
        
        __syncthreads();
        
        // process N
        if (global_w < w && y != 0 && y < ymax && is_conn(s_buf[idx], s_buf[idx - BLOCK_SIZE_X], t)) {
          findAndUnion(s_buf2, idx, idx - BLOCK_SIZE_X);
        }

//        __syncthreads();        
        
        // process NE
        if (global_w < w-1 && y != 0 && (x != BLOCK_SIZE_X - 1) && y < ymax && is_conn(s_buf[idx], s_buf[idx - BLOCK_SIZE_X + 1], t)) {
          findAndUnion(s_buf2, idx, idx - BLOCK_SIZE_X + 1);
        }
        
        __syncthreads();        
        
        // process W
        if (global_w < w && x != 0 && y < ymax && is_conn(s_buf[idx], s_buf[idx -  1], t)) {
          findAndUnion(s_buf2, idx, idx - 1);
        }
        
    }
    
    __syncthreads();
    
    // copy result back
    for(int i=0; i<LOCAL_CCL_Y_IT; ++i) {
      if (global_w >= w) continue;
        
      int y = (threadIdx.y + LOCAL_CCL_Y_STEP*i);
        int idx = BLOCK_SIZE_X * y + threadIdx.x;
        
        int v = find(s_buf2, idx);
        
        // convert v to global space
        int v_x = v % BLOCK_SIZE_X;
        int v_y = v / BLOCK_SIZE_X;
      d_dst_img[offset + w * y + BLOCK_SIZE_X * blockIdx.x + threadIdx.x] = offset + blockIdx.x * BLOCK_SIZE_X + v_y * w + v_x;
    }    
    
}

void debugTime(char* label, Timer& timer) {
    printf("%s: %f\n", label, timer.getTimeElapsed());
}

void debugTime(char* label, int number, Timer& timer) {
    printf("%s %d: %f\n", label, number, timer.getTimeElapsed());
}

int roundUp(int v, int a) {
    if (v%a==0) return v;
    return v + a - v % a;
}

int divRoundUp(int a, int b) {
  int rounding = (a % b == 0) ? 0 : 1;
    return a / b + rounding;
}

void initDevice() {
  
    int count = 0;
    cudaGetDeviceCount(&count);
/*  printf("devices: %d\n", count);

    for(int i=0; i<count; ++i) {
      cudaDeviceProp prop;
        cudaGetDeviceProperties(&prop, i);
        
        printf("device %d = %s\n", i, prop.name);
    }*/
    cudaSetDevice(0);
    checkCUDAError("init");
}


void CCL::cuda_neighbour_process() {

  Timer t_k_neigh, t_k_seams, t_k_final;
    Timer t_cpy_to, t_cpy_from;
  Timer t_cuda;
    Timer t_alloc, t_free;
    
    Timer t;
    t.start();

#ifdef DEVICE_QUERY
    cudaDeviceProp props;    
    cudaGetDeviceProperties(&props, 0);
    printf("totalGlobalMem=%d\nsharedMemPerBlock=%d\nregsPerBlock=%d\n", props.totalGlobalMem, props.sharedMemPerBlock, props.regsPerBlock);
    printf("cap=%d.%d canMapHostMemory=%d deviceOvelap=%d\n", props.major, props.minor, props.canMapHostMemory, props.deviceOverlap);
  checkCUDAError("propquery");
#endif    

  initDevice();

//    const int BLOCK_SIZE = 32;
    
    int *p_img_d_unaligned = 0;
    int *p_img_dst = 0;    
    
    int img_size = m_W * m_H * sizeof(int);
    int img_unaligned_size = (roundUp(m_H, BLOCK_SIZE_Y) * m_W + BLOCK_SIZE_X) * sizeof(int);

  t_alloc.start();
    cudaMalloc( (void**)&p_img_d_unaligned, img_unaligned_size );
    cudaMalloc( (void**)&p_img_dst, img_unaligned_size );    
    checkCUDAError("malloc0");
    t_alloc.stopAndAddTime();
    
    printf("after alloc: %f\n", t.getTimeElapsed());
    
    // copy BLOCK_SIZE lines to the device
    t_cpy_to.start();
    cudaMemcpy( &p_img_d_unaligned[0], &(*m_result)[0], img_size, cudaMemcpyHostToDevice);
  checkCUDAError("copy hostToDevice");        
    t_cpy_to.stopAndAddTime();
    
    int yBlocks = divRoundUp(m_H, BLOCK_SIZE_Y);
    int xBlocks = divRoundUp(m_W, BLOCK_SIZE_X);

  t_cuda.start();
    
    printf("before kernels: %f\n", t.getTimeElapsed());

    // start local ccl calculation for blocks
  t_k_neigh.start();    
    dim3 dimGrid(xBlocks, yBlocks);
    dim3 dimBlock(BLOCK_SIZE_X, LOCAL_CCL_THREADS/BLOCK_SIZE_X);
  
    int s_mem = sizeof(int) * BLOCK_SIZE_X * BLOCK_SIZE_Y * 2;

    if (m_diag == false) {
        calculate_neighbours4_cuda<<< dimGrid, dimBlock, s_mem >>>(&p_img_d_unaligned[0], &p_img_dst[0], m_W, m_thresh, m_H);
    } else {    
        calculate_neighbours8_cuda<<< dimGrid, dimBlock, s_mem >>>(&p_img_d_unaligned[0], &p_img_dst[0], m_W, m_thresh, m_H);
    }
  cudaThreadSynchronize();    
    t_k_neigh.stopAndAddTime();
    
    // join the local ccl calculation to global
    t_k_seams.start();
    dim3 dimBlockJoin(max(BLOCK_SIZE_Y, BLOCK_SIZE_X));
    if (m_diag == false) {
        join_seams4_cuda<<< dimGrid, dimBlockJoin >>>(&p_img_d_unaligned[0], &p_img_dst[0], m_W, m_thresh, m_H);
    } else {
        join_seams8_cuda<<< dimGrid, dimBlockJoin >>>(&p_img_d_unaligned[0], &p_img_dst[0], m_W, m_thresh, m_H);
    }
  cudaThreadSynchronize();    
    t_k_seams.stopAndAddTime();

  // final pass
    t_k_final.start();    
    dim3 dimFinalBlock(FINAL_BLOCK_SIZE_X, FINAL_PASS_THREADS/FINAL_BLOCK_SIZE_X);
    dim3 dimFinalGrid(divRoundUp(m_W, FINAL_BLOCK_SIZE_X), divRoundUp(m_H, FINAL_BLOCK_SIZE_Y));
    final_find_cuda<<< dimFinalGrid, dimFinalBlock >>>(&p_img_dst[0], m_W, m_H);    
    cudaThreadSynchronize();
    t_k_final.stopAndAddTime();
    
    printf("after kernels: %f\n", t.getTimeElapsed());
    
    t_cuda.stopAndAddTime();
 
//  t_wait_for_sync.start();
//  cudaThreadSynchronize();
//  t_wait_for_sync.stopAndAddTime();
                
    // copy image from the device
    t_cpy_from.start();
    cudaMemcpy( &(*m_result)[0], &p_img_dst[0], img_size, cudaMemcpyDeviceToHost);
  checkCUDAError("copy deviceToHost");    
    t_cpy_from.stopAndAddTime();
 
  t_free.start();
    cudaFree(p_img_d_unaligned);
    cudaFree(p_img_dst);
    t_free.stopAndAddTime();
#ifdef PRINT_TIMINGS    
    printf("time for cuda_neighbour_process(): %f\n", t.getTimeElapsed());
    printf(" thereof alloc:          %f\n", t_alloc.getTimeElapsed());    
    printf(" thereof copy  to:       %f\n", t_cpy_to.getTimeElapsed());    
    printf(" thereof kernel neigh:   %f\n", t_k_neigh.getTimeElapsed());
    printf(" thereof kernel seams:   %f\n", t_k_seams.getTimeElapsed());    
    printf(" thereof kernel final:   %f\n", t_k_final.getTimeElapsed());    
//    printf(" thereof wait for sync:  %f\n", t_wait_for_sync.getTimeElapsed());
    printf(" thereof copy  from:     %f\n", t_cpy_from.getTimeElapsed());
    printf(" thereof free:           %f\n", t_free.getTimeElapsed());
    printf("total gpu time:          %f\n", t_cuda.getTimeElapsed());
#endif

}


int main( int, char** ) {
  CCL ccl;
  QImage image1( "../inputs/cerberus_enhanced.bmp" );
  vector <int> image;
  for (int i=0; i<image1.width(); ++i ) {
    for (int j=0; j<image1.height(); ++j ) {
      image.push_back( image1.pixel(i,j) );
    }
  }
  vector <int> result = ccl.cuda_ccl( image, image1.width() , 4, 42);
  QImage image2;
  for (int i=0; i<image1.width(); ++i ) {
    for (int j=0; j<image1.height(); ++j ) {
      image2.setPixel( i, j, result[ (j*image1.width())+j] );
    }
  }
  image2.save("result.jpg");
}