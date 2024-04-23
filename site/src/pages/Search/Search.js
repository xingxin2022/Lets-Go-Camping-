import "./Search.css";
import Header from "../../components/Header/Header.jsx";
import Footer from "../../components/Footer/Footer";
import { useState, useEffect } from "react";
import PopUpModal from '../../components/PopUpModal/PopUpModal';
import ParkList from "../../components/ParkList/ParkList";
import { useUser } from '../../UserContext';
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
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");
  const [start, setStart] = useState(0);
  const [searchType, setSearchType] = useState("parkname");
  const [allParkCodes, setAllParkCodes] = useState([]);
  const [currentParkIndex, setCurrentParkIndex] = useState(0);

//  const [currentUser, setCurrentUser] = useState(null);
  const { currentUser, setCurrentUser } = useUser();
  const [userFavorites, setUserFavorites] = useState([]);
  const [modalIsOpen, setIsOpen] = useState(false);
  const [modalPark, setModalPark] = useState([]);
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

  const handleClick = (e) => {
        console.log("Clicked", e.target);
        const searchType = e.target.getAttribute('data-type');
        const query = e.target.getAttribute('data-query');
        setQuery(query);
        setSearchType(searchType);
        closeModal()
        performSearch(searchType, query, 0);
  };

  const performSearch = (searchType, query, start) => {
        fetch("/api/search/search-parks", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                query: query,
                searchType: searchType,
                startPosition: start,
            }),
        })
            .then((response) => response.json())
            .then((response) => {
                if (response?.data) {
                    const updatedParks = start === 0 ? response.data : [...parks, ...response.data];
                    setParks(updatedParks);
                    setStart(start);

//                    setParks(response.data);
//                    console.log(response.data);
//                    setStart(start)
                }
            });
  }


//  useEffect(() => {
//      fetch('/api/users/current-user', {
//          method: 'GET',
//          credentials: 'include'
//      })
//      .then(response => {
//          if(response.ok) {
//              return response.text();
//          } else {
//              throw new Error('Not logged in');
//          }
//      })
//      .then(username => {
//          setCurrentUser(username);
//      })
//      .catch(error => {
//          console.error('Error:', error);
//          setCurrentUser(null);
//      });
//  }, []);

  useEffect(() => {
      const fetchUserFavorites = async () => {
          try {
              const response = await fetch(`/api/search/get-user-favorites?username=${currentUser}`, {
                  method: 'GET',
                  credentials: 'include',
                  headers: {
                      'Content-Type': 'application/json'
                  },
              });

              if (!response.ok) {
                  // Check if the HTTP response is not successful and throw an error with the status code
                  throw new Error(`Network response was not ok, status: ${response.status}`);
              }

              const text = await response.text(); // Get the response as text to safely parse it later
              console.log("Response text:", text); // Log the response text to see what's actually returned

              if (!text) {
                  console.log("No favorites found, or empty response from server.");
                  setUserFavorites([]); // Assume no favorites exist, update state accordingly
                  return; // Exit the function without an error
              }

              try {
                  const favorites = JSON.parse(text); // Safely parse the JSON text
                  setUserFavorites(favorites); // Update the state or perform an action with the favorites
              } catch (parseError) {
                  // Catch any errors that occur during JSON parsing
                  throw new Error(`Failed to parse JSON: ${parseError.message} | Raw response: ${text}`);
              }

          } catch (error) {
              // Catch any network errors or the errors thrown from the above blocks
              console.error('Failed to fetch user favorites', error);
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
      <div className="page-container">
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
              onClick={() => performSearch(searchType, query, 0)
              }
          >
              Search
          </button>
          </div>
          <div className="search-results">
              <ParkList parks={displayParks} onSetShowPark = {
              handleShowPark} currentUser={currentUser} setUserFavorites={setUserFavorites} userFavorites={userFavorites}/>
          </div>
          {modalIsOpen && modalPark && (
              <PopUpModal currentUser={currentUser} modalIsOpen={modalIsOpen} closeModal={closeModal} park={modalPark} handleClick={handleClick} setUserFavorites={setUserFavorites} userFavorites={userFavorites} />
          )}

          {parks && parks.length > 0 && (
              <button className="show-more-button"
                  onClick={() => {
                      const newStart = start + 10;
                      performSearch(searchType, query, newStart);
                  }}
              >
                  Show 10 more results
              </button>
          )}
          <Footer/>
      </div>
  );
}


export default Search;
