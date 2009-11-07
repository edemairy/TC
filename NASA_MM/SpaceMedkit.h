//! See NASA topcoder marathon match problem statement.

#include <string>
#include <vector>
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
    static bool orderRid( const MedicalResource& r1, const MedicalResource& r2) {
        return (r1.rid < r2.rid);
    }
};


class MedicalEvent {
  public:
    string mid;
    int    best;
    int    worst;
    static bool orderRid( const MedicalEvent& e1, const MedicalEvent& e2) {
        return (e1.mid < e2.mid);
    }
};

class Mission {
  public:
    string mission;
    int    order;
    string mid;
    bool   isWorst;
    int    treated;
    int    untreated;
};

set<MedicalResource, MedicalResource::orderRid > medicalResources;
set<MedicalEvent, MedicalResource::orderRid >    medicalEvents;



vector <string> SpaceMedkit::getMedkit(
            vector <string> availableResources,
            vector <string> requiredResources,
            vector <string> missions,
            double P,
            double C)
{
}

