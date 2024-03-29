import "./PopUpModal.css";
import React from "react";
import Modal from 'react-modal';

function PopUpModal({modalIsOpen, closeModal, park}) {

    return (
     <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        contentLabel="Example Modal"
      >
        <div className="content">
        <h2>{park?.fullName}  {park.isFavorite ? " 🌟️ " : ""}</h2>

        <img src={park.images && park.images[0] ? park.images[0].url : ""}
             alt={ park.images && park.images[0] ? park.images[0].altText : "Park image"}/>

        <p>Address: {park.addresses && park.addresses[0]
                                         ? park.addresses[0].line1 + ', '+ park.addresses[0].city + ', '+park.addresses[0].stateCode+', ' + park.addresses[0].countryCode
                                         : "Address not available"}</p>
        <p>Website: {park.url ? <a href={park.url} target="_blank" rel="noopener noreferrer">{park.url}</a> : "URL not available"} </p>
        <p>Entrance Fees: {park.entranceFees && park.entranceFees[0]
                                             ? `$${park.entranceFees[0].cost}`
                                             : "Entrance fee not available"}</p>
        <p>Description: {park.description ? park.description : "Description not applicable"}</p>
                <p>Activities: {park.activities
                                         ? park.activities.map((activity) => activity.name).join(", ")
                                         : "Activities not applicable"}</p>
        <button onClick={closeModal}>close</button>
        </div>
      </Modal>
    );
}

export default PopUpModal;
