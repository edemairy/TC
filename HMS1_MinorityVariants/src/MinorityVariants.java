
import java.io.StringReader;
import java.util.Scanner;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
/**
 *
 * @author edemairy
 */
public class MinorityVariants {
    final static Logger logger = Logger.getLogger(MinorityVariants.class.getName());
    private short[] readquality;
    private double[] kmerAbundance;
    private short[] mappingScore;

    public int[] classifyReads(String[] reads) {
        int[] result = new int[0];
        readInputs(reads);
        return result;
    }

    /**
     *
     * @param reads Each line is described as: "contain the values of 3 metrics for this read separated by single space characters: read quality, k-mer
     * abundance and mapping score, in this exact order".
     */
    protected void readInputs(String[] reads) {
        initData(reads.length);
        int cpt = 0;
        for (String line : reads) {
            Scanner scanner = new Scanner(line);
            getReadquality()[cpt] = scanner.nextShort();
            getKmerAbundance()[cpt] = scanner.nextDouble();
            getMappingScore()[cpt] = scanner.nextShort();
            ++cpt;
        }
    }

    private void initData(int length) {
        logger.entering(MinorityVariants.class.getName(), "initData", length);

        setReadquality(new short[length]);
        setKmerAbundance(new double[length]);
        setMappingScore(new short[length]);

        logger.exiting(MinorityVariants.class.getName(), "initData", length);
    }

    /**
     * @return the readquality
     */
    public short[] getReadquality() {
        return readquality;
    }

    /**
     * @param readquality the readquality to set
     */
    public void setReadquality(short[] readquality) {
        this.readquality = readquality;
    }

    /**
     * @return the kmerAbundance
     */
    public double[] getKmerAbundance() {
        return kmerAbundance;
    }

    /**
     * @param kmerAbundance the kmerAbundance to set
     */
    public void setKmerAbundance(double[] kmerAbundance) {
        this.kmerAbundance = kmerAbundance;
    }

    /**
     * @return the mappingScore
     */
    public short[] getMappingScore() {
        return mappingScore;
    }

    /**
     * @param mappingScore the mappingScore to set
     */
    public void setMappingScore(short[] mappingScore) {
        this.mappingScore = mappingScore;
    }


}
