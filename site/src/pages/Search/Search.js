import "./Search.css";
import Header from "../../components/Header/Header.jsx";
import { useState, useEffect } from "react";
import PopUpModal from '../../components/PopUpModal.js';
import ParkList from "../../components/ParkList/ParkList";
//import Button from "react-bootstrap/Button";
//import "bootstrap/dist/css/bootstrap.min.css";


const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
const initialParks = [
  {
    index: "1",
    id: "6DA17C86-088E-4B4D-B862-7C1BD5CF236B",
    url: "https://www.nps.gov/acad/index.htm",
    fullName: "Acadia National Park",
    parkCode: "acad",
    addresses: [
      {
        postalCode: "04609",
        city: "Bar Harbor",
        stateCode: "ME",
        countryCode: "US",
        provinceTerritoryCode: "",
        line1: "25 Visitor Center Road",
        type: "Physical",
        line3: "",
        line2: "Hulls Cove Visitor Center",
      },
    ],
    description:
      "Acadia National Park protects the natural beauty of the highest rsits a year, it's one of the top 10 most-visited national parks in the United States.",
    entranceFees: [
      {
        cost: "6.00",
        description:
          "Vehicle reservations are not required for any other areas of the park, or for visitors who enter the area by foot, bike, or taxi. Vehicle reservations provide a timed entry, but do not require a departure time until 10 pm, when the road closes to vehicles. Reservations do not permit re-entry. Reservations are per vehicle, not per person. Reservations do not assign a specific parking space. Parking is prohibited outside of designated spaces. Cadillac is not served by the Island Explorer bus system.",
        title: "Timed Entry Reservation - Location",
      },
    ],
    images: [
      {
        credit: "NPS / Kristi Rugg",
        title: "Acadia's rocky coastline",
        altText:
          "Large puffy clouds dot a brilliant blue sky as wave crash against the rocky coastline of Acadia.",
        caption:
          "Millions of people come to Acadia for our distinctive rocky coastline.",
        url: "https://www.nps.gov/common/uploads/structured_data/3C7B45AE-1DD8-B71B-0B7EE131C7DFC2F5.jpg",
      },
    ],
    activities: [
      {
        id: "09DF0950-D319-4557-A57E-04CD2F63FF42",
        name: "Arts and Culture",
      },
    ],
    favorite: "True",
  },
  {
    index: "2",
    id: "6DA17C86-088E-4B4D-B862-7C1BD5CF236",
    url: "https://www.nps.gov/badl/index.htm",
    fullName: "Badlands National Park",
    parkCode: "badl",
    addresses: [
      {
        postalCode: "04609",
        city: "Bar Harbor",
        stateCode: "ME",
        countryCode: "US",
        provinceTerritoryCode: "",
        line1: "25216 Ben Reifel Road",
        type: "Physical",
        line3: "",
        line2: "Hulls Cove Visitor Center",
      },
    ],
    description:
      "The rugged beauty of the Badlands draws visitors from around the world. These striking geologic deposits contain one of the world’s richest fossil beds.",
    entranceFees: [
      {
        cost: "30.00",
        description:
          "Vehicle reservations are not required for any other areas of the park, or for visitors who enter the area by foot, bike, or taxi. Vehicle reservations provide a timed entry, but do not require a departure time until 10 pm, when the road closes to vehicles. Reservations do not permit re-entry. Reservations are per vehicle, not per person. Reservations do not assign a specific parking space. Parking is prohibited outside of designated spaces. Cadillac is not served by the Island Explorer bus system.",
        title: "Timed Entry Reservation - Location",
      },
    ],
    images: [
      {
        credit: "NPS / Kristi Rugg",
        title: "Acadia's rocky coastline",
        altText:
          "Large puffy clouds dot a brilliant blue sky as wave crash against the rocky coastline of Acadia.",
        caption:
          "Millions of people come to Acadia for our distinctive rocky coastline.",
        url: "https://www.nps.gov/common/uploads/structured_data/3C82EE63-1DD8-B71B-0BD6EE0FDCB5D402.jpg",
      },
    ],
    activities: [
      {
        id: "09DF0950-D319-4557-A57E-04CD2F63FF42",
        name: "Arts and Culture",
      },
    ],
    favorite: "True",
  },
];

function Search() {
  const [parks, setParks] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");
  const [start, setStart] = useState(0);
  const [searchType, setSearchType] = useState("parkname");
  const [allParkCodes, setAllParkCodes] = useState([]);
  const [currentParkIndex, setCurrentParkIndex] = useState(0);
  const [currentUser, setCurrentUser] = useState(null);
  const [userFavorites, setUserFavorites] = useState([]);
  const [modalIsOpen, setIsOpen] = useState(false);
  const [modalPark, setModalPark] = useState(null);
  const [displayParks, setDisplayParks] = useState([]);


  function openModal() {
      setIsOpen(true);
  }


  function closeModal() {
      setIsOpen(false);
  }

  const handleShowPark = (park) => {
        setModalPark(park);
        openModal();
    };





  useEffect(() => {
      fetch('/api/users/current-user', {
          method: 'GET',
          credentials: 'include'
      })
      .then(response => {
          if(response.ok) {
              return response.text();
          } else {
              throw new Error('Not logged in');
          }
      })
      .then(username => {
          setCurrentUser(username);
      })
      .catch(error => {
          console.error('Error:', error);
          setCurrentUser(null);
      });
  }, []);

  useEffect(() => {
      const fetchUserFavorites = async () => {
         if (currentUser){
             try{
                const response = await fetch(`/api/search/get-user-favorites?username=${currentUser}`, {
                     method: 'GET',
                     credentials: 'include',
                     headers: {
                         'Content-Type': 'application/json'
                     },
                });
                const favorites = await response.json();
                setUserFavorites(favorites || []);
             } catch (error) {
                console.error('Failed to fetch user favorites', error);
             }
         }
      };
      fetchUserFavorites();
  }, [currentUser]);

  useEffect(() => {
      const updatedParks = parks.map(park => ({
          ...park,
          isFavorite: userFavorites.includes(park.parkCode),
      }));
      setDisplayParks(updatedParks);
  }, [parks, userFavorites]);

  return (
      <div>
          <Header/>

          <br></br>
          <br></br>
          <br></br>
          <br></br>

          <div className="search-container">
          <div className="option-container">
              <label htmlFor="searchType">Search By:</label>
              <label>
                  <input
                      type="radio"
                      id="parkName"
                      name="searchType"
                      value="parkname"
                      checked={searchType === "parkname"}
                      onChange={(e) => setSearchType(e.target.value)}
                  />
                  Park Name
              </label>

              <label>
                  <input
                      type="radio"
                      id="amenity"
                      name="searchType"
                      value="amenities"
                      checked={searchType === "amenities"}
                      onChange={(e) => setSearchType(e.target.value)}
                  />
                  Amenity
              </label>

              <label>
                  <input
                      type="radio"
                      id="state"
                      name="searchType"
                      value="states"
                      checked={searchType === "states"}
                      onChange={(e) => setSearchType(e.target.value)}
                  />
                  State
              </label>

              <label>
                  <input
                      type="radio"
                      id="activity"
                      name="searchType"
                      value="activities"
                      checked={searchType === "activities"}
                      onChange={(e) => setSearchType(e.target.value)}
                  />
                  Activity
              </label>
          </div>
          <input
              style={{textAlign: "center"}}
              id="searchInput"
              className="search"
              type="text"
              placeholder={`Search national park by ${searchType}...`}
              value={query}
              onChange={(e) => setQuery(e.target.value)}
          />

          <button
              onClick={() => {
                  const newStart = 0;
                  fetch("/api/search/search-parks", {
                      method: "Post",
                      headers: {
                          "Content-Type": "application/json",
                      },
                      body: JSON.stringify({
                          query: query,
                          searchType: searchType,
                          startPosition: newStart ,
                      }),
                  })
                      .then((response) => response.json())
                      .then((response) => {
                          if (response?.data) {
                              setParks(response.data);
                              setStart(newStart)
                          }
                      });
              }}
          >
              Search
          </button>
          </div>
          <div className="search-results">
              <ParkList parks={displayParks} onSetShowPark = {
              handleShowPark} currentUser={currentUser} setUserFavorites={setUserFavorites} userFavorites={userFavorites}/>
          </div>
          {modalIsOpen && modalPark && (
              <PopUpModal modalIsOpen={modalIsOpen} closeModal={closeModal} park={modalPark}  />
          )}

          {parks && parks.length > 0 && (
              <button
                  onClick={() => {
                      const newStart = start + 10;
                      fetch("/api/search/search-parks", {
                          method: "Post",
                          headers: {
                              "Content-Type": "application/json",
                          },
                          body: JSON.stringify({
                              query: query,
                              searchType: searchType,
                              startPosition: newStart,
                          }),
                      })
                          .then((response) => response.json())
                          .then((response) => {
                              if (response?.data) {
                                  setParks([...parks, ...response.data]);
                                  setStart(newStart);
                              }
                          });
                  }}
              >
                  Show 10 more results
              </button>
          )}
      </div>
  );
}

//function ParkList({ parks, onSetShowPark, currentUser, setUserFavorites, userFavorites }) {
//  const uniqueParks = Array.from(
//    new Map(parks.map((park) => [park.id, park])).values()
//  );
//  return (
//    <div className="parks-container">
//      {uniqueParks.map((park) => (
//        <Park park={park} key={park.id} onSetShowPark = {onSetShowPark} currentUser={currentUser} setUserFavorites={setUserFavorites} userFavorites={userFavorites} />
//      ))}
//    </div>
//  );
//}
//
//function Park({ park, onSetShowPark, currentUser, setUserFavorites, userFavorites }) {
//const [favoriteConfirmation, setFavoriteConfirmation] = useState("");
//  return (
//  <div class="park-container">
//      <h3 className="park-name" onClick = {()=>onSetShowPark(park)}>
//      {park.fullName}  {park.isFavorite ? " 🌟️ " : ""}
//      </h3>
//      <img
//        src={park.images && park.images[0] ? park.images[0].url : ""}
//        alt={
//          park.images && park.images[0] ? park.images[0].altText : "Park image"
//        }
//      />
//      <p>Address: {park.addresses && park.addresses[0]
//                              ? park.addresses[0].line1 + ', '+ park.addresses[0].city + ', '+park.addresses[0].stateCode+', ' + park.addresses[0].countryCode
//                              : "Address not available"}</p>
//
//      <button
//        onClick={() => {
//            fetch("/api/search/add-favorite", {
//                method: "Post",
//                headers: {
//                    "Content-Type": "application/json",
//                },
//                body: JSON.stringify({
//                    userName: currentUser,
//                    parkCode: park.parkCode,
//                }),
//            })
//                .then((response) => response.json())
//                .then((response) => {
//                    if (response?.message === "Park successfully added to favorite list") {
//                        setUserFavorites([...userFavorites, park.parkCode])
//                    }
//                    return response;
//                })
//                .then((response) => {
//                    if (response?.message === "Park successfully added to favorite list") {
//                        setFavoriteConfirmation('Park successfully added to favorite list');
//                        setTimeout(() => setFavoriteConfirmation(''), 3000);
//                    }
//                    if (response?.message === "Park already in the favorite list") {
//                        setFavoriteConfirmation('Park already in the favorite list');
//                        setTimeout(() => setFavoriteConfirmation(''), 3000);
//                    }
//                })
//                .catch(error => console.error('Error:', error));
//        }}
//      >
//         Add to favorite list
//      </button>
//      {favoriteConfirmation && <div className="confirmation-message">{favoriteConfirmation}</div>}
//      </div>
//  );
//}

export default Search;
