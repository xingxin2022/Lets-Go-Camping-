import React, { useState } from 'react';
import './Park.css';

function Park({ park, onSetShowPark, currentUser, setUserFavorites, userFavorites , testId}) {
  const [favoriteConfirmation, setFavoriteConfirmation] = useState("");
  return (
    <div data-testid={testId} className="park-container">
          <h3 className="park-name" onClick = {()=>onSetShowPark(park)}>
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

          <button
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
                        isPrivate: true,
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
          {favoriteConfirmation && <div className="confirmation-message">{favoriteConfirmation}</div>}
          </div>
  );
}

export default Park;
