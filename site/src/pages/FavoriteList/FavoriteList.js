import "./FavoriteList.css";
import { useState, useEffect } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";

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

    // {
    //     index: "3",
    //     id: "",
    //     url: "",
    //     fullName: "",
    //     parkCode: "badl",
    //     addresses: [
    //         {
    //             postalCode: "",
    //             city: "",
    //             stateCode: "",
    //             countryCode: "",
    //             provinceTerritoryCode: "",
    //             line1: "",
    //             type: "",
    //             line3: "",
    //             line2: "",
    //         },
    //     ],
    //     description:
    //         "",
    //     entranceFees: [
    //         {
    //             cost:'',
    //             description:
    //                 "",
    //             title: "",
    //
    //         },
    //     ],
    //     images: [
    //         {
    //             credit: "",
    //             title: "",
    //             altText:
    //                 "",
    //             caption:
    //                 "",
    //             url: "",
    //         },
    //     ],
    //     activities: [],
    //     favorite: "",
    // },
];
function ConfirmModal({ isOpen, onClose, onConfirm }) {
    return (
        <Modal show={isOpen} onHide={onClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Confirm Action</Modal.Title>
            </Modal.Header>
            <Modal.Body>Are you sure you want to delete all?</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={onConfirm}>
                    Confirm
                </Button>
            </Modal.Footer>
        </Modal>
    );
}
function FavoriteList() {
    const [parks, setParks] = useState(initialParks);//
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [visibility, setVisibility] = useState('private');
    const [isVisibilityModalOpen, setIsVisibilityModalOpen] = useState(false);

    function handleDeleteAll() {
        setParks(() => []);
    }

    function openModal() {
        setIsModalOpen(true);
    }

    function closeModal() {
        setIsModalOpen(false);
    }

    function confirmDelete() {
        handleDeleteAll();
        closeModal();
    }
    function openVisibilityModal() {
        setIsVisibilityModalOpen(true);
    }
    function updateVisibility(newVisibility) {
        if (newVisibility) {
            setVisibility(newVisibility);
        }
        setIsVisibilityModalOpen(false); // Close the modal after selection
    }

    function VisibilityModal({ isOpen, onChoose }) {
        return (
            <Modal show={isOpen} onHide={() => onChoose()} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Select Visibility</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="d-flex justify-content-around">
                        <Button variant="secondary" onClick={() => onChoose('private')}>
                            Private
                        </Button>
                        <Button variant="primary" onClick={() => onChoose('public')}>
                            Public
                        </Button>
                    </div>
                </Modal.Body>
            </Modal>
        );
    }

    return (
        <div style={{ textAlign: "center" }}>
            <div className="favorite-results">
                <div className="headers">
                    <p className="headerName">Favorite Parks</p>
                    <div className="visibilityContainer">
                        <span className="visibility">Visibility: </span>
                        <button className="visibilityButton" onClick={openVisibilityModal}>{visibility}</button>
                    </div>
                </div>

                <ParkList parks={parks} setParks={setParks}/>
                {parks.some((park) => park.favorite === "True") && (
                    <button onClick={openModal}>Delete All</button>
                )}
                <ConfirmModal isOpen={isModalOpen} onClose={closeModal} onConfirm={confirmDelete} />
                <VisibilityModal isOpen={isVisibilityModalOpen} onChoose={updateVisibility} />
            </div>
        </div>
    );
}

function ParkList({ parks, setParks }) {
    //need change after backend is ready
    function handleMoveUp(index) {
        setParks((currentParks) => {
            const newParks = [...currentParks];
            const parkIndex = newParks.findIndex((park) => park.index === index);
            if (parkIndex > 0) {
                const [itemToMove] = newParks.splice(parkIndex, 1);
                newParks.splice(parkIndex - 1, 0, itemToMove);
            }
            return newParks;
        });
    }
    //need change after backend is ready
    function handleMoveDown(index) {
        setParks((currentParks) => {
            const newParks = [...currentParks];
            const parkIndex = newParks.findIndex((park) => park.index === index);
            if (parkIndex < newParks.length - 1) {
                const [itemToMove] = newParks.splice(parkIndex, 1);
                newParks.splice(parkIndex + 1, 0, itemToMove);
            }
            return newParks;
        });
    }

    const uniqueParks = Array.from(
        new Map(parks.map((park) => [park.id, park])).values()
    );
    return (
        <ol>
            {uniqueParks.map((park) => (
                <Park
                    park={park}
                    key={park.index}
                    setParks={setParks}
                    handleMoveUp={handleMoveUp}
                    handleMoveDown={handleMoveDown}
                />
            ))}
        </ol>
    );
}

function Park({ park, setParks, handleMoveUp, handleMoveDown }) {
    const [showModal, setShowModal] = useState(false);
    const [actionType, setActionType] = useState("");

    const handleClose = () => setShowModal(false);
    const handleShow = (action) => {
        setActionType(action);
        setShowModal(true);
    };

    const confirmAction = () => {
        if (actionType === "delete") {
            setParks((parks) => parks.filter((p) => p.index !== park.index));
        } else if (actionType === "moveUp") {
            handleMoveUp(park.index);
        } else{ // (actionType === "moveDown")
            handleMoveDown(park.index);
        }
        handleClose();
    };

    return (
        <div className="park-container">
            {/* <li> */}
            {/* <h3> {park.index}</h3> */}
            <h3>{park.fullName}</h3>
            <img
                src={park.images[0].url}
                alt={
                    park.images[0].altText
                }
            />
            <p>Address:</p>
            <ul>
                <li>
                    {park.addresses[0].line1
                        }
                </li>
            </ul>

            <p>Website:</p>
            <ul>
                <li>{ park.url }</li>
            </ul>

            <p>Entrance Fees:</p>
            <ul>
                <li>
                    { `$${park.entranceFees[0].cost}`
                        }
                </li>
            </ul>

            <p>Description:</p>
            <ul>
                <li>
                    {park.description}
                </li>
            </ul>

            <p>Activities:</p>
            <ul>
                <li>
                    { park.activities.map((activity) => activity.name).join(", ")
                        }
                </li>
            </ul>

            <ul>
                <li>{"Favorited" }</li>
            </ul>

            {/* //need change after backend is ready */}
            {/* <button onClick={() => handleDelectedFavorited(park.index)}>
        Remove From Favorite List
      </button>
      <button onClick={() => handleMoveUp(park.index)} Alert>
        ⬆️
      </button> */}
            {/* <button onClick={() => handleMoveDown(park.index)}>⬇️</button> */}
            <div className="d-flex justify-content-around">
                <button className="btn btn-light" onClick={() => handleShow("moveUp")}>
                    Move Up ⬆️
                </button>
                <button className="btn btn-light" onClick={() => handleShow("moveDown")}>
                    Move Down ⬇️
                </button>
                <button className="btn btn-secondary" onClick={() => handleShow("delete")}>
                    Remove From Favorites ❌
                </button>
            </div>

            <Modal show={showModal} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Action</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {`Are you sure you want to ${
                        actionType === "delete"
                            ? "remove this park from favorites"
                            : "move this park"
                    }?`}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={confirmAction}>
                        Confirm
                    </Button>
                </Modal.Footer>
            </Modal>
            {/* </li> */}
        </div>
    );
}

export default FavoriteList;