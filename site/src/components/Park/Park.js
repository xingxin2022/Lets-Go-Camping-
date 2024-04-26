import React, { useState } from 'react';
import './Park.css';

function Park({ park, onSetShowPark, currentUser, setUserFavorites, userFavorites , testId}) {
  const [favoriteConfirmation, setFavoriteConfirmation] = useState("");
  const [showAddToFavorites, setShowAddToFavorites] = useState(false);
  return (
    <div  id="park-card" data-testid={testId} className="park-container" onMouseEnter={() => setShowAddToFavorites(true)} onMouseLeave={() => setShowAddToFavorites(false)}>
          <h3 id="park-name" className="park-name" onClick = {()=>onSetShowPark(park)}>
          {park.fullName}  {park.isFavorite ? " 🌟️ " : ""}
          </h3>
          <img
            src={park.images && park.images[0] ? park.images[0].url : ""}
            alt={
              park.images && park.images[0] ? park.images[0].altText : "Park image"
            }
          />
          <p>Address: {park.addresses && park.addresses[0]
                                  ? park.addresses[0].line1 + ', '+ park.addresses[0].city + ', '+park.addresses[0].stateCode+', ' + park.addresses[0].countryCode
                                  : "Address not available"}</p>

          {showAddToFavorites && (<button
                  data-testid="addToFav"
            onClick={() => {
                fetch("/api/search/add-favorite", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        userName: currentUser,
                        parkCode: park.parkCode,
                        parkName: park.fullName,
                        isPublic: false,
                    }),
                })
                    .then((response) => response.json())
                    .then((response) => {
                        if (response?.message === "Park successfully added to favorite list") {
                            setUserFavorites([...userFavorites, park.parkCode])
                        }
                        return response;
                    })
                    .then((response) => {
                        if (response?.message === "Park successfully added to favorite list") {
                            setFavoriteConfirmation('Park successfully added to favorite list');
                            setTimeout(() => setFavoriteConfirmation(''), 3000);
                        }
                        if (response?.message === "Park already in the favorite list") {
                            setFavoriteConfirmation('Park already in the favorite list');
                            setTimeout(() => setFavoriteConfirmation(''), 3000);
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }}
          >
             +
          </button>
          )}
          {favoriteConfirmation && <div  id="confirmation-message"  className="confirmation-message">{favoriteConfirmation}</div>}
          </div>
  );
}

export default Park;
