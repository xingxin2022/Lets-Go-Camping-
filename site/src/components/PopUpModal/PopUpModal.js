import "./PopUpModal.css";
import React, { useState } from "react";
import Modal from 'react-modal';

function PopUpModal({ currentUser, modalIsOpen, closeModal, park, handleClick, setUserFavorites, userFavorites}) {
    const [favoriteConfirmation, setFavoriteConfirmation] = useState("");
    return (
     <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        contentLabel="Example Modal"
      >
        <div data-testid="modal" className="content">
        <h2 onClick={closeModal}>{park?.fullName}  {park.isFavorite ? " 🌟️ " : ""}</h2>

        <img src={park.images && park.images[0] ? park.images[0].url : ""}
             alt={ park.images && park.images[0] ? park.images[0].altText : "Park image"}/>

        <p data-testid="handleClick-states" data-type="states" data-query={park.addresses?.[0]?.stateCode} onClick={handleClick}>Address: {park.addresses && park.addresses[0]
                                         ? park.addresses[0].line1 + ', '+ park.addresses[0].city + ', '+park.addresses[0].stateCode+', ' + park.addresses[0].countryCode
                                         : "Address not available"}</p>
        <p>Website: {park.url ? <a href={park.url} target="_blank" rel="noopener noreferrer">{park.url}</a> : "URL not available"} </p>
        <p>Entrance Fees: {park.entranceFees && park.entranceFees[0]
                                             ? `$${park.entranceFees[0].cost}`
                                             : "Entrance fee not available"}</p>
        <p >Description: {park.description ? park.description : "Description not applicable"}</p>
        <p>Amenities:</p>
        <div>
          {park.amenities && park.amenities.length > 0 ? park.amenities.map((amenity, index) => (
            <span
              data-testid={`handleClick-${index}`}
              key={index}
              className="clickable"
              data-type="amenities"
              data-query={amenity.name}
              onClick={handleClick}>
              {amenity.name}
              {index < park.amenities.length - 1 ? ', ' : ''}
            </span>
          )) : "Amenities not applicable"}
        </div>

        <p>Activities:</p>
        <div>
          {park.activities && park.activities.length > 0 ? park.activities.map((activity, index) => (
            <span
              key={index}
              className="clickable"
              data-type="activities"
              data-query={activity.name}
              onClick={handleClick}>
              {activity.name}
              {index < park.activities.length - 1 ? ', ' : ''}
            </span>
          )) : "Activities not applicable"}
        </div>
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
      </Modal>
    );
}

export default PopUpModal;
