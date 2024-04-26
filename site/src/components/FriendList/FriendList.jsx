import React, { useState, useEffect } from 'react';
import styles from './FriendList.module.css';
import { useUser } from '../../UserContext';
import PopUpModal from '../../components/PopUpModal/PopUpModal';

function FriendList() {
    const { currentUser, setCurrentUser } = useUser();
    const [usernames, setUsernames] = useState([]);
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [favoriteParksDisplay, setFavoriteParksDisplay] = useState([]);
    const [errorMessage, setErrorMessage] = useState("");
    const [modalIsOpen, setIsOpen] = useState(false);
    const [modalPark, setModalPark] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    const [searchError, setSearchError] = useState("");
    const [hoveredPark, setHoveredPark] = useState(null);
    const [userFavorites, setUserFavorites] = useState([]); // Added state for user favorites

    function openModal() {
        setIsOpen(true);
    }

    function closeModal() {
        setIsOpen(false);
    }

    const handleShowPark = (parkCode) => {
        performSearch(parkCode);
    };

    const handleClick = (e) => {
        console.log("Clicked", e.target);
        const searchType = e.target.getAttribute('data-type');
        const query = e.target.getAttribute('data-query');
        setQuery(query);
        setSearchType(searchType);
        closeModal();
    };

    const performSearch = (parkCode) => {
        console.log("Searching for park with code:", parkCode);
        fetch("/api/search/search-park-by-id", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                parkCode: parkCode
            }),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Failed to fetch park details');
                }
                return response.json();
            })
            .then((response) => {
                if (response?.data) {
                    console.log(response.data[0]);
                    const updatedParks = response.data[0];
                    setModalPark(updatedParks);
                    openModal();
                }
            })
            .catch(error => console.error('Failed to fetch park details:', error));
    };

    const toggleUserSelection = (username) => {
        setSelectedUsers(prevSelected =>
            prevSelected.includes(username)
                ? prevSelected.filter(e => e !== username)
                : [...prevSelected, username]
        );
    };

    const handleCompareClick = async () => {
        setFavoriteParksDisplay([]);  // Clear the current display
        setErrorMessage("");  // Clear any previous error messages

        console.log(selectedUsers);
        fetch('/api/users/favorite-parks/union', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedUsers)
        })
            .then(response => {
                const contentType = response.headers?.get("content-type") ?? "";
                if (contentType.includes("application/json")) {
                    return response.json().then(data => ({
                        status: response.status,
                        body: data
                    }));
                } else {
                    return response.text().then(text => {
                        throw new Error(text || 'Response is not OK');
                    });
                }
            })
            .then(({ status, body }) => {
                if (status === 200) {
                    setFavoriteParksDisplay(body);
                    console.log(body);
                } else {
                    throw new Error(body.message || 'Unknown error occurred');
                }
            })
            .catch(error => {
                console.error('Error fetching union of favorite parks:', error);
                setErrorMessage(error.message); // Display the error message from the server
            });
    };

    const handleInputChange = (e) => {
        setSearchInput(e.target.value);
        setSearchError(""); // Clear error when user starts typing
    };

    const handleSearch = () => {
        const userExists = usernames.includes(searchInput);
        if (userExists) {
            toggleUserSelection(searchInput);
            setSearchError(""); // Clear any previous error messages
        } else {
            setSearchError("User not found.");
        }
        setSearchInput(""); // Optionally clear the input after search
    };

    useEffect(() => {
        const fetchUsernames = async () => {
            fetch('/api/users/get-users')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not OK');
                    }
                    return response.json();
                })
                .then(data => setUsernames(data))
                .catch(error => console.error('Error fetching users:', error));
        };

        fetchUsernames();
    }, []);

    return (
        <div>
            <div className={styles.friendListContainer}>
                <input
                    type="text"
                    placeholder="Search user by username"
                    value={searchInput}
                    onChange={handleInputChange}
                    className={styles.searchInput}
                />
                <button onClick={handleSearch} className={styles.button}>
                    Search
                </button>
                {searchError && <div className={styles.errorMessage}>{searchError}</div>}

                <br></br>

                {usernames.map((username, index) => (
                    <div
                        key={index}
                        data-testid={`user-${username}`}
                        className={`${styles.friendItem} ${selectedUsers.includes(username) ? styles.active : ''}`}
                        onClick={() => toggleUserSelection(username)}
                    >
                        <div className={styles.friendItemIcon}/>
                        <div className={styles.friendItemContent}>
                            <h4>{username}</h4>
                        </div>
                    </div>
                ))}
                <button className={styles.button} onClick={handleCompareClick}> Compare now</button>
            </div>

            <div className={styles.textContainer} data-testid="message-display">
                {favoriteParksDisplay.map((park, index) => (
                    <li key={index} className={styles.parkItem} data-testid={`park-item-${park.parkCode}`}>
                        <span onClick={() => handleShowPark(park.parkCode)}
                              onMouseEnter={() => {
                                  console.log("Hovering over park:", park.parkCode); // Log on hover
                                  setHoveredPark(park.parkCode);
                              }}
                              onMouseLeave={() => setHoveredPark(null)}
                              data-testid={`park-name-${park.parkCode}`}>
                            {park.parkName} ({park.ratio.toFixed(2)}%)
                        </span>
                        {hoveredPark === park.parkCode && (
                            <div className={`${styles.tooltip}`}
                                 data-testid={`tooltip-${park.parkCode}`}>
                                {park.usernames ? park.usernames.join(', ') : 'No users'}
                            </div>
                        )}
                    </li>
                ))}
                {errorMessage && <div className="error-message">{errorMessage}</div>}
            </div>

            {modalIsOpen && modalPark && (
                <PopUpModal currentUser={currentUser} modalIsOpen={modalIsOpen} closeModal={closeModal} park={modalPark}
                            handleClick={handleClick} setUserFavorites={setUserFavorites}
                            userFavorites={userFavorites}/>
            )}
        </div>
    );
}

export default FriendList;
