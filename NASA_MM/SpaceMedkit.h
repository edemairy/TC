//! See NASA topcoder marathon match problem statement.

#include <string>
#include <vector>
#include <set>
#include <map>
#include <cassert>
#include <sstream>
using namespace std;

//#define DEBUG_INPUT 0

class MedicalResource {
  public:
    string rid;
    bool   isConsumable;
    double    mass;
    double    volume;
    MedicalResource(): rid("not initialized"), isConsumable(false),mass(-1), volume(-1) {}
    MedicalResource( string i_medicalResourceString ) {
      istringstream is( i_medicalResourceString );
      is >> rid >> isConsumable >> mass >> volume;
    }
};
typedef map<string, MedicalResource> MedicalResources;
ostream& operator <<( ostream& os, const MedicalResource& i_medicalResource ) {
    os << "rid=" << i_medicalResource.rid << ", isConsumable=" << i_medicalResource.isConsumable <<
            ", mass=" << i_medicalResource.mass << ", volume=" << i_medicalResource.volume;
    return os;
}

class MedicalEvent {
  public:
    string mid;
    string rid;
    double    best;
    double    worst;
    MedicalEvent( string i_medicalResourceString ) {
        istringstream is( i_medicalResourceString );
        is >> mid >> rid >> best >> worst;
    }
};
typedef map<string, MedicalEvent > MedicalOneResourceEvent;
typedef map<string, MedicalOneResourceEvent>    MedicalEvents;
ostream& operator <<( ostream& os, const MedicalEvent& i_medicalEvent ) {
    os << "mid=" << i_medicalEvent.mid << "rid=" << i_medicalEvent.rid << ", best=" << i_medicalEvent.best <<
            ", worst=" << i_medicalEvent.worst;
    return os;
}

class Mission {
  public:
    int    mission;
    int    order;
    string mid;
    bool   isWorst;
    int    treated;
    int    untreated;
    Mission( string i_missionString ) {
        istringstream is( i_missionString );
        is >> mission >> order >> mid >> isWorst >> treated >> untreated;
    }
};
typedef map<int, Mission >      MedicalMissionEvent;
typedef map<int, MedicalMissionEvent > MedicalMissions;ostream& operator <<( ostream& os, const Mission& i_mission ) {
    os << "mission=" << i_mission.mission << ", order=" << i_mission.order <<
            ", mid=" << i_mission.mid << ", isWorst=" << i_mission.isWorst <<
            ", treated=" << i_mission.treated << ", untreated=" << i_mission.untreated;
    return os;
}

class UsedResource {
  public:
    UsedResource() : rid("not initialized"), volume(-1) {};
    UsedResource( string i_rid, double i_volume ): rid( i_rid ), volume( i_volume ){}
    string rid;
    double volume;
};
UsedResource& operator+=( UsedResource& i_resource, double i_volume ) {
  i_resource.volume += i_volume;
  return i_resource;
}

typedef map< string, UsedResource > UsedResources;

class SpaceMedkit {
  public:
    //! \param availableResources Medical resources: vector of "RID CONSUMABLE MASS VOLUME"
    //! \param requiredResources  Medical events: vector of "MID RID BEST WORST"
    //! \param missions           Several missions for training. "MISSION ORDER MID WORST TREATED UNTREATED"
    //! \param P                  Maximal evacuation rate.
    //! \param C                  Weight on the volume, used for score computation.
    //! \return                   Medical resources to carry for the missions: vector of "RID QUANTITY"
    vector <string> getMedkit(
            vector <string>& availableResources,
            vector <string>& requiredResources,
            vector <string>& missions,
            double P,
            double C);

  private:
    MedicalResources medicalResources;
    MedicalEvents    medicalEvents;
    MedicalMissions  missions;

    void fillMedicalResources( const vector<string> i_availableResources );
    void fillEvents(           const vector<string> i_requiredResources  );
    void fillMissions(         const vector<string> i_missions           );
    void computeWorstCase( UsedResources& o_usedResources );
    void computeBestCase(  UsedResources& o_usedResources );
    void computeWorstFromHistoryCase( UsedResources& o_usedResources );
    vector<string> convertToVectorString( const UsedResources& i_usedResources );

};
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void SpaceMedkit::fillMedicalResources( const vector<string> i_availableResources ) {
    for (int i=0; i < i_availableResources.size(); ++i ) {
        MedicalResource newMedicalResource( i_availableResources[i] );
        medicalResources.insert( make_pair(newMedicalResource.rid, newMedicalResource) );
    }
#ifdef DEBUG_INPUT
    for (MedicalResources::iterator it = medicalResources.begin(); it != medicalResources.end(); ++it ) {
        cerr << it->second << endl;
    }
#endif
    assert( i_availableResources.size() == medicalResources.size() );
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void SpaceMedkit::fillEvents(           const vector<string> i_requiredResources  ) {
    for (int i=0; i < i_requiredResources.size(); ++i ) {
        MedicalEvent newMedicalEvent( i_requiredResources[i] );
        medicalEvents[ newMedicalEvent.mid ].insert(make_pair( newMedicalEvent.rid, newMedicalEvent ));
    }
#ifdef DEBUG_INPUT
    for (MedicalEvents::iterator it = medicalEvents.begin(); it != medicalEvents.end(); ++it ) {
      for (MedicalOneResourceEvent::iterator it1 = it->second.begin(); it1 != it->second.end(); ++it1 ) {
        cerr << it1->second << endl;
      }
    }
#endif
//    assert( i_requiredResources.size() == medicalEvents.size() );
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void SpaceMedkit::fillMissions(         const vector<string> i_missions           ) {
    for (int i=0; i < i_missions.size(); ++i ) {
        Mission newMission( i_missions[i] );
        missions[ newMission.mission].insert( make_pair( newMission.order, newMission ));
    }
#ifdef DEBUG_INPUT
    for( MedicalMissions::iterator it = missions.begin(); it != missions.end(); ++it ) {
      for( MedicalMissionEvent::iterator it1 = it->second.begin(); it1 != it->second.end(); ++it1 ) {
        cerr << it1->second << endl;
      }
    }
#endif
   // assert( i_missions.size() == missions.size() );
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//! Strategy used: for each medical resource, the maximal number of resources needed for all the training missions is returned.
void SpaceMedkit::computeWorstCase( UsedResources& o_usedResources )
{
    UsedResources emptyResources;
    for( MedicalResources::iterator it = medicalResources.begin(); it != medicalResources.end(); ++it ) {
        string id = it->second.rid;
        UsedResource newResource( id, 0 );
        emptyResources[ newResource.rid ] = newResource ;
    }
    o_usedResources = emptyResources;
    for (MedicalMissions::iterator it = missions.begin(); it != missions.end(); ++it ) {
      UsedResources currentMissionUsedResources( emptyResources );
      for( MedicalMissionEvent::iterator it1 = it->second.begin(); it1 != it->second.end(); ++it1 ) {
        Mission currentMissionEvent = it1->second;
        for ( MedicalOneResourceEvent::iterator it2 = medicalEvents[ currentMissionEvent.mid ].begin();
              it2 != medicalEvents[ currentMissionEvent.mid ].end(); ++it2 )
        {
              MedicalEvent currentMedicalEvent = it2->second;
              double volumeToAdd = max( currentMedicalEvent.worst, currentMedicalEvent.best );
              if ( medicalResources[ currentMedicalEvent.rid ].isConsumable ) {
                currentMissionUsedResources[ currentMedicalEvent.rid ] += volumeToAdd;
              } else {
                currentMissionUsedResources[ currentMedicalEvent.rid ].volume = volumeToAdd;
              }
        }
      }
      for( MedicalResources::iterator it = medicalResources.begin(); it != medicalResources.end(); ++it ) {
        string id = it->second.rid;
        o_usedResources[ id ].volume = max( o_usedResources[ id ].volume, currentMissionUsedResources[id].volume );
      }
    }
    assert( o_usedResources.size() == medicalResources.size() );
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void SpaceMedkit::computeBestCase( UsedResources& o_usedResources )
{
    UsedResources emptyResources;
    for( MedicalResources::iterator it = medicalResources.begin(); it != medicalResources.end(); ++it ) {
        string id = it->second.rid;
        UsedResource newResource( id, 0 );
        emptyResources[ newResource.rid ] = newResource ;
    }
    o_usedResources = emptyResources;
    for (MedicalMissions::iterator it = missions.begin(); it != missions.end(); ++it ) {
      UsedResources currentMissionUsedResources( emptyResources );
      for( MedicalMissionEvent::iterator it1 = it->second.begin(); it1 != it->second.end(); ++it1 ) {
        Mission currentMissionEvent = it1->second;
        for ( MedicalOneResourceEvent::iterator it2 = medicalEvents[ currentMissionEvent.mid ].begin();
              it2 != medicalEvents[ currentMissionEvent.mid ].end(); ++it2 )
        {
              MedicalEvent currentMedicalEvent = it2->second;
              if ( medicalResources[ currentMedicalEvent.rid ].isConsumable ) {
                currentMissionUsedResources[ currentMedicalEvent.rid ] += currentMedicalEvent.best;
              } else {
                currentMissionUsedResources[ currentMedicalEvent.rid ].volume = currentMedicalEvent.best;
              }
        }
      }
      for( MedicalResources::iterator it = medicalResources.begin(); it != medicalResources.end(); ++it ) {
        string id = it->second.rid;
        o_usedResources[ id ].volume = max( o_usedResources[ id ].volume, currentMissionUsedResources[id].volume );
      }
    }
    assert( o_usedResources.size() == medicalResources.size() );
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void SpaceMedkit::computeWorstFromHistoryCase( UsedResources& o_usedResources )
{
    UsedResources emptyResources;
    for( MedicalResources::iterator it = medicalResources.begin(); it != medicalResources.end(); ++it ) {
        string id = it->second.rid;
        UsedResource newResource( id, 0 );
        emptyResources[ newResource.rid ] = newResource ;
    }
    o_usedResources = emptyResources;
    for (MedicalMissions::iterator it = missions.begin(); it != missions.end(); ++it ) {
      UsedResources currentMissionUsedResources( emptyResources );
      for( MedicalMissionEvent::iterator it1 = it->second.begin(); it1 != it->second.end(); ++it1 ) {
        Mission currentMissionEvent = it1->second;
        for ( MedicalOneResourceEvent::iterator it2 = medicalEvents[ currentMissionEvent.mid ].begin();
              it2 != medicalEvents[ currentMissionEvent.mid ].end(); ++it2 )
        {
              MedicalEvent currentMedicalEvent = it2->second;
              double volumeToAdd = currentMissionEvent.isWorst ? currentMedicalEvent.worst : currentMedicalEvent.best;
              if ( medicalResources[ currentMedicalEvent.rid ].isConsumable ) {
                currentMissionUsedResources[ currentMedicalEvent.rid ] += volumeToAdd;
              } else {
                currentMissionUsedResources[ currentMedicalEvent.rid ].volume = volumeToAdd;
              }
              o_usedResources[ currentMedicalEvent.rid ].volume = max( o_usedResources[ currentMedicalEvent.rid ].volume, currentMissionUsedResources[currentMedicalEvent.rid].volume );
        }
      }
    }
    assert( o_usedResources.size() == medicalResources.size() );
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
vector<string> SpaceMedkit::convertToVectorString( const UsedResources& i_usedResources )
{
  vector<string> result;
  for ( UsedResources::const_iterator it = i_usedResources.begin(); it != i_usedResources.end(); ++it ) {
    ostringstream newResourceString;
    newResourceString << it->second.rid << " " << it->second.volume;
    result.push_back( newResourceString.str() );
    cerr << newResourceString.str() << endl;
  }
  cerr << "result.size()=" << result.size() << endl;
  return  result;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//! Definition of SpaceMedkit::getMedkit
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
vector <string> SpaceMedkit::getMedkit(
            vector <string>& availableResources,
            vector <string>& requiredResources,
            vector <string>& missions,
            double P,
            double C)
{
  fillMedicalResources( availableResources );
  fillEvents(           requiredResources  );
  fillMissions(         missions           );
  UsedResources usedResources;
  computeWorstFromHistoryCase( usedResources );
  vector<string> result = convertToVectorString( usedResources );
  return result;
}

