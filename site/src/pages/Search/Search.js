import "./Search.css";
import Header from "../../components/Header/Header.jsx";
import { useState } from "react";




const stateCodes = {
  Alabama: "AL",
  Alaska: "AK",
  Arizona: "AZ",
  Arkansas: "AR",
  California: "CA",
  Colorado: "CO",
  Connecticut: "CT",
  Delaware: "DE",
  Florida: "FL",
  Georgia: "GA",
  Hawaii: "HI",
  Idaho: "ID",
  Illinois: "IL",
  Indiana: "IN",
  Iowa: "IA",
  Kansas: "KS",
  Kentucky: "KY",
  Louisiana: "LA",
  Maine: "ME",
  Maryland: "MD",
  Massachusetts: "MA",
  Michigan: "MI",
  Minnesota: "MN",
  Mississippi: "MS",
  Missouri: "MO",
  Montana: "MT",
  Nebraska: "NE",
  Nevada: "NV",
  "New Hampshire": "NH",
  "New Jersey": "NJ",
  "New Mexico": "NM",
  "New York": "NY",
  "North Carolina": "NC",
  "North Dakota": "ND",
  Ohio: "OH",
  Oklahoma: "OK",
  Oregon: "OR",
  Pennsylvania: "PA",
  "Rhode Island": "RI",
  "South Carolina": "SC",
  "South Dakota": "SD",
  Tennessee: "TN",
  Texas: "TX",
  Utah: "UT",
  Vermont: "VT",
  Virginia: "VA",
  Washington: "WA",
  "West Virginia": "WV",
  Wisconsin: "WI",
  Wyoming: "WY",
};

function Search() {
  const [parks, setParks] = useState([]);
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");
  const [start, setStart] = useState(0);
  const [searchType, setSearchType] = useState("");
  const [allParkCodes, setAllParkCodes] = useState([]);
  const [currentParkIndex, setCurrentParkIndex] = useState(0);

//  async function fetchParkDetails(parkCode, startPosition) {
//    const url = `https://developer.nps.gov/api/v1/parks?limit=10&start=${startPosition}&parkCode=${parkCode}&api_key=${key}`;
//    const response = await fetch(url);
//    const data = await response.json();
//    return data.data[0];
//  }
//
//  async function fetchParks(startPosition = start) {
//    try {
//      setIsLoading(true);
//      setError("");
//      let search_url;
//      let formattedQuery = query.replace(/\s+/g, "_");
//
//      switch (searchType) {
//        case "parkname":
//          search_url = `https://developer.nps.gov/api/v1/parks?q=${formattedQuery}&limit=10&start=${startPosition}&api_key=${key}`;
//          break;
//        case "activities":
//          if (startPosition === 0) {
//            search_url = `https://developer.nps.gov/api/v1/activities/parks?limit=50&q=${formattedQuery}&api_key=${key}`;
//            const res = await fetch(search_url);
//            const data = await res.json();
//            console.log(data.data);
//            const parkCodes =
//              data.data[0]?.parks.map((park) => park.parkCode) || [];
//            setAllParkCodes(parkCodes);
//            const initialParkCodes = parkCodes.slice(0, 10);
//            fetchParkDetailsBatch(startPosition, initialParkCodes);
//            setCurrentParkIndex(10);
//          } else {
//            const newParkCodes = allParkCodes.slice(
//              startPosition,
//              startPosition + 10
//            );
//            fetchParkDetailsBatch(startPosition, newParkCodes);
//            setCurrentParkIndex((prevIndex) => prevIndex + 10);
//          }
//          break;
//        case "states":
//          const stateCode = stateCodes[query];
//          if (!stateCode) throw new Error(`Invalid state name: ${query}`);
//          search_url = `https://developer.nps.gov/api/v1/parks?limit=10&start=${startPosition}&stateCode=${stateCode}&api_key=${key}`;
//          break;
//        case "amenities":
//          if (startPosition === 0) {
//            search_url = `https://developer.nps.gov/api/v1/amenities/parksplaces?q=${formattedQuery}&limit=50&api_key=${key}`;
//            const amenityRes = await fetch(search_url);
//            const amenityData = await amenityRes.json();
//            const parkCodes = amenityData.data[0]
//              ?.map((group) => group.parks.map((park) => park.parkCode))
//              .flat();
//
//            setAllParkCodes(parkCodes);
//            if (parkCodes.length > 0) {
//              const initialParkCodes = parkCodes.slice(0, 10);
//              await fetchParkDetailsBatch(startPosition, initialParkCodes);
//              setCurrentParkIndex(10);
//            }
//          } else {
//            const newParkCodes = allParkCodes.slice(
//              startPosition,
//              startPosition + 10
//            );
//            fetchParkDetailsBatch(startPosition, newParkCodes);
//            setCurrentParkIndex((prevIndex) => prevIndex + 10);
//          }
//
//          break;
//        default:
//          break;
//      }
//
//      console.log("Fetching URL: ", search_url);
//
//      if (searchType !== "activities" && searchType !== "amenities") {
//        const res = await fetch(search_url);
//        if (!res.ok) throw new Error("Something went wrong when fetching data");
//        const data = await res.json();
//        console.log(data.data);
//        console.log("Start : ", startPosition);
//        setParks(startPosition !== 0 ? [...parks, ...data.data] : data.data);
//      }
//    } catch (e) {
//      setError(e.message);
//    } finally {
//      setIsLoading(false);
//    }
//  }
//
//  async function fetchParkDetailsBatch(startPosition, parkCodes) {
//    setIsLoading(true);
//    const parksInfo = [];
//    for (let parkCode of parkCodes) {
//      try {
//        const url = `https://developer.nps.gov/api/v1/parks?parkCode=${parkCode}&limit=1&sort=-relevanceScore&api_key=${key}`;
//        const response = await fetch(url);
//        const data = await response.json();
//        console.log("fetch detail park: ", data.data);
//        if (data.data.length > 0) {
//          parksInfo.push(data.data[0]);
//        }
//      } catch (error) {
//        console.error(
//          "Error fetching park details for park code:",
//          parkCode,
//          error
//        );
//      }
//    }
//    setParks(startPosition !== 0 ? [...parks, ...parksInfo] : parksInfo);
//    setIsLoading(false);
//  }
//
//  function handleMoreResults() {
//    const newStart = start + 10;
//    setStart(newStart);
//    fetchParks(newStart);
//  }


//  function handleMoreResults() {
//    const newStart = start + 10;
//    setStart(newStart);
//    fetchParksFromBackend();
//  }

  return (
      <div>
          <Header/>

          <br></br>
          <br></br>
          <br></br>
          <br></br>

          {/* <label htmlFor="searchInput">Search:</label> */}

          {/* <select
        id="searchType"
        value={searchType}
        onChange={(e) => setSearchType(e.target.value)}
      >
        <option value="parks">Park Name</option>
        <option value="amenities">Amenity</option>
        <option value="states">State</option>
        <option value="activities">Activity</option>
      </select> */}

          <div style={{textAlign: "center"}}>
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
                  fetch("/search-parks", {
                      method: "Post",
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
                              setParks(response.data);
                          }
                      });
              }}
          >
              Search
          </button>
          <div className="search-results">
              <ParkList parks={parks}/>
              {/* // {isLoading && <Loader />}
        // {!isLoading && !error && parks && <ParkList parks={parks} />}
        // {error && <ErrorMessage message={error} />} */}
          </div>
          {parks && parks.length > 0 && (
//        <button onClick={handleMoreResults}> Show 10 more results </button>
              <button
                  onClick={() => {
                      fetch("/search-parks", {
                          method: "Post",
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
                                  setParks([...parks, ...response.data]);
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

function ParkList({ parks }) {
  const uniqueParks = Array.from(
    new Map(parks.map((park) => [park.id, park])).values()
  );
  return (
    <ol>
      {uniqueParks.map((park) => (
        <Park park={park} key={park.id} />
      ))}
    </ol>
  );
}

function Park({ park }) {
  return (
    <li>
      <h3>{park.fullName}</h3>
      <img
        src={park.images && park.images[0] ? park.images[0].url : ""}
        alt={
          park.images && park.images[0] ? park.images[0].altText : "Park image"
        }
      />
      <p>Address:</p>
      <ul>
        <li>
          {park.addresses && park.addresses[0]
            ? park.addresses[0].line1
            : "Address not available"}
        </li>
      </ul>

      <p>Website:</p>
      <ul>
        <li>{park.url ? park.url : "URL not available"}</li>
      </ul>

      <p>Entrance Fees:</p>
      <ul>
        <li>
          {park.entranceFees && park.entranceFees[0]
            ? `$${park.entranceFees[0].cost}`
            : "Entrance fee not available"}
        </li>
      </ul>

      <p>Description:</p>
      <ul>
        <li>
          {park.description ? park.description : "Description not applicable"}
        </li>
      </ul>

      <p>Activities:</p>
      <ul>
        <li>
          {park.activities
            ? park.activities.map((activity) => activity.name).join(", ")
            : "Activities not applicable"}
        </li>
      </ul>
    </li>
  );
}

export default Search;
