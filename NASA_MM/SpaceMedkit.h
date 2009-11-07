//! See NASA topcoder marathon match problem statement.

#include <string>
#include <vector>
#include <set>
using namespace std;

class SpaceMedkit {
  public:
    //! \param availableResources Medical resources: vector of "RID CONSUMABLE MASS VOLUME"
    //! \param requiredResources  Medical events: vector of "MID RID BEST WORST"
    //! \param missions           Several missions for training. "MISSION ORDER MID WORST TREATED UNTREATED"
    //! \param P                  Maximal evacuation rate.
    //! \param C                  Weight on the volume, used for score computation.
    //! \return                   Medical resources to carry for the missions: vector of "RID QUANTITY"
    vector <string> getMedkit(
            vector <string> availableResources,
            vector <string> requiredResources,
            vector <string> missions,
            double P,
            double C);
};

class MedicalResource {
  public:
    string rid;
    bool   isConsumable;
    int    mass;
    int    volume;
    struct orderRid {
      bool operator()( const MedicalResource& r1, const MedicalResource& r2) {
        return (r1.rid < r2.rid);
      }
    };
};


class MedicalEvent {
  public:
    string mid;
    int    best;
    int    worst;
    struct  orderRid {
      bool operator()( const MedicalEvent& e1, const MedicalEvent& e2) {
        return (e1.mid < e2.mid);
      }
    };
};

class Mission {
  public:
    string mission;
    int    order;
    string mid;
    bool   isWorst;
    int    treated;
    int    untreated;
    struct orderMission{
      bool operator()( const MedicalEvent& e1, const MedicalEvent& e2) {
        return (e1.mid < e2.mid);
      }
    };
};

set<MedicalResource, MedicalResource::orderRid > medicalResources;
set<MedicalEvent, MedicalResource::orderRid >    medicalEvents;
set<Mission>                                     missions;

vector <string> SpaceMedkit::getMedkit(
            vector <string> availableResources,
            vector <string> requiredResources,
            vector <string> missions,
            double P,
            double C)
{
  //fillMedicalResources( availableResources );
  //fillEvents(           requiredResources  );
  //fillMissions(         missions           );
  vector<string> result;
  return result;
}

