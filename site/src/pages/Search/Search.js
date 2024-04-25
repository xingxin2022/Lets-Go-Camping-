import "./Search.css";
import Header from "../../components/Header/Header.jsx";
import Footer from "../../components/Footer/Footer";
import { useState, useEffect } from "react";
import PopUpModal from '../../components/PopUpModal/PopUpModal';
import ParkList from "../../components/ParkList/ParkList";
import { useUser } from '../../UserContext';

function Search() {
    const [parks, setParks] = useState([]);
    const [error, setError] = useState("");
    const [query, setQuery] = useState("");
    const [start, setStart] = useState(0);
    const [searchType, setSearchType] = useState("parkname");
    const [allParkCodes, setAllParkCodes] = useState([]);
    const [currentParkIndex, setCurrentParkIndex] = useState(0);

    const { currentUser, setCurrentUser } = useUser();
    const [userFavorites, setUserFavorites] = useState([]);
    const [modalIsOpen, setIsOpen] = useState(false);
    const [modalPark, setModalPark] = useState([]);
    const [displayParks, setDisplayParks] = useState([]);

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            performSearch(searchType, query, 0);
        }
    };

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
                }
            });
    }


    useEffect(() => {
        const fetchUserFavorites = async () => {
            try{
                const response = await fetch(`/api/search/get-user-favorites?username=${currentUser}`, {
                    method: 'GET',
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                });
                const favorites = await response.json();
                setUserFavorites(favorites );

            } catch (error) {
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
        <div className="page-container" data-testid="Search">
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
                    onKeyPress={handleKeyPress}
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
                <button id="load-more-button" className="show-more-button"
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