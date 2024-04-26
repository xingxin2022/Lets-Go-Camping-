import "./FavoriteList.css";
import { useState, useEffect } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { useUser } from "../../UserContext";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
//import 'bootstrap/dist/css/bootstrap.min.css';
import PopUpModal from '../../components/PopUpModal/PopUpModal';



function ConfirmModal({ isOpen, onClose, onConfirm, title, children }) {
    return (
        <Modal show={isOpen} onHide={onClose} centered>
            <Modal.Header>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{children}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" id="cancel" onClick={onClose}>
                    Cancel
                </Button>
                <Button variant="primary" id="confirm" onClick={onConfirm}>
                    Confirm
                </Button>
            </Modal.Footer>
        </Modal>
    );
}



function Park({ park, onMoveUp, onMoveDown, onDelete, onShowPark }) {
    const [showDelete, setShowDelete] = useState(false);
//    console.log(park);
    return (
        <div className="park-box" id="park-box" onMouseEnter={() => setShowDelete(true)} onMouseLeave={() => setShowDelete(false)}>
            <h3 onClick={() => onShowPark(park)}>{park.fullName}</h3>
            {park.images && park.images[0] && (
                <img src={park.images[0].url} alt={park.fullName} style={{ width: "50%", height: "auto" }} />
            )}
            <div className="button-group">
                {showDelete && <Button variant="primary" id="moveup" onClick={() => onMoveUp()}>Move Up ⬆️</Button>}
                {showDelete && <Button variant="primary" id="movedown" onClick={() => onMoveDown()}>Move Down ⬇️</Button>}
                {showDelete && <Button variant="danger" id="remove" onClick={() => onDelete()}>Remove ❌</Button>}
            </div>
        </div>

    );
}

function FavoriteList() {
    const [parks, setParks] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedPark, setSelectedPark] = useState(null);
    const [actionType, setActionType] = useState('');
    const [isPublic, setIsPublic] = useState(null);
    const {currentUser } = useUser();
    const [modalIsOpen, setIsOpen] = useState(false);
    const [modalPark, setModalPark] = useState([]);
    const [userFavorites, setUserFavorites] = useState([]);

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

      const handleClick = () => {
              console.log("This search not supported");
//              const searchType = e.target.getAttribute('data-type');
//              const query = e.target.getAttribute('data-query');
//              setQuery(query);
//              setSearchType(searchType);
//              closeModal();
//              performSearch(searchType, query, 0);
        };


    useEffect(() => {
        fetchParks();
        fetchPublic();
    }, [currentUser]);

    async function fetchPublic() {
            try {
                const response = await fetch(`/api/favorites/fetchPublicStatus?username=${currentUser}`, { method: 'POST' });
                const data = await response.json();
                setIsPublic(data);
            } catch (error) {
                console.error('Failed to fetch parks', error);
            }
    }

    async function fetchParks() {
        try {
            const response = await fetch(`/api/favorites/getFavorites?username=${currentUser}`, { method: 'POST' });
            const data = await response.json();
            if (data.parks) {
                fetchParkDetails(data.parks.map(park => park.parkCode));
            }
            else{
                console.error('No favorite list');
            }
        } catch (error) {
            console.error('Failed to fetch parks', error);
        }
    }

    async function fetchParkDetails(parkCodes) {
        try {
            const response = await fetch(`/api/favorites/fetchDetails`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({ parkCodes })
            });
            const result = await response.json();
            if (result.success) {
                setParks(result.parks);
            } else {
                console.error('Failed to fetch park details:', result.message);
            }
        } catch (error) {
            console.error('Error fetching park details:', error);
        }
    }

    const toggleVisibility = async () => {
        console.log("old" , isPublic);
        console.log("new" , !isPublic);
        try {
            const response = await fetch(`/api/favorites/updatePrivacy?username=${currentUser}&isPublic=${!isPublic}`, {
                method: 'POST'
            });
            if (response.ok) {
                console.log(toggleVisibility);
                console.log(response);

                setIsPublic(!isPublic); // Update visibility state on successful response
            } else {
                throw new Error('Failed to update visibility');
            }
        } catch (error) {
            console.error('Visibility update failed:', error);
        }
    };


    function handleShowModal(parkCode, action) {
        setSelectedPark(parkCode);
        setActionType(action);
        setIsModalOpen(true);
    }

    async function confirmAction() {
//        if (!selectedPark && actionType !== 'deleteAll') {
//            console.error('No park selected for the action');
//            return;
//        }

        try {
            await handleParkAction(selectedPark, actionType);
        } catch (error) {
            console.error('Failed to execute park action:', error);
            return; // Prevent modal from closing if the action fails
        }

        setIsModalOpen(false);
    }

    async function handleParkAction(parkCode, action) {
        const index = parks.findIndex(park => park.parkCode === parkCode);
        const actionUrlMap = {
            moveUp: `/api/favorites/movePark?username=${currentUser}&parkCode=${parkCode}&moveUp=true`,
            moveDown: `/api/favorites/movePark?username=${currentUser}&parkCode=${parkCode}&moveUp=false`,
            delete: `/api/favorites/removePark?username=${currentUser}&parkCode=${parkCode}`,
            deleteAll: `/api/favorites/deleteAll?username=${currentUser}`
        };

        const response = await fetch(actionUrlMap[action], { method: 'POST' });
        if (!response.ok) {
            throw new Error('Operation failed');
        }

        updateLocalParks(index, action);
    }

    function updateLocalParks(index, action) {
        let newParks = [...parks];
        switch (action) {
            case 'moveUp':
                if (index > 0) {
                    [newParks[index], newParks[index - 1]] = [newParks[index - 1], newParks[index]];
                }
                break;
            case 'moveDown':
                if (index < parks.length - 1) {
                    [newParks[index], newParks[index + 1]] = [newParks[index + 1], newParks[index]];
                }
                break;
            case 'delete':
                newParks.splice(index, 1);
                break;
            case 'deleteAll':
                newParks = [];
                break;
//            default:
//                throw new Error('Invalid action');
        }
        setParks(newParks);
    }

    return (
        <div className="page-container" >
            <Header />
            <div className="main-content" style={{textAlign: "center"}}>
                <div className="visibilityContainer">
                    <Button className="visibilityButton" variant={isPublic ? "primary" : "secondary"} onClick={toggleVisibility}>
                        {!isPublic ? "Set Public" : "Set Private"}
                    </Button>
                </div>
                <h1>Favorite Parks</h1>
                {parks.length > 0 && (
                    <Button className="btn btn-danger" onClick={() => handleShowModal(null, 'deleteAll')}>Delete All</Button>
                )}
                <div data-testid="park-list">
                {parks.map((park, index) => (
                    <Park
                        key={park.parkCode}
                        park={park}
                        onMoveUp={() => handleShowModal(park.parkCode, 'moveUp')}
                        onMoveDown={() => handleShowModal(park.parkCode, 'moveDown')}
                        onDelete={() => handleShowModal(park.parkCode, 'delete')}
                        onShowPark = {() => handleShowPark(park)}
                    />
                ))}
                </div>
                <ConfirmModal
                    isOpen={isModalOpen}
                    onClose={() => setIsModalOpen(false)}
                    onConfirm={confirmAction}
                    title="Confirm Action"
                    children={`Are you sure you want to ${
                        actionType === 'delete' ? 'remove this park from favorites' :
                            actionType === 'moveUp' ? 'move this park up' :
                                actionType === 'moveDown' ? 'move this park down' : 'delete all parks'}?`}
                />
            </div>
            {modalIsOpen && modalPark && (
                  <PopUpModal currentUser={currentUser} modalIsOpen={modalIsOpen} closeModal={closeModal} park={modalPark} handleClick={handleClick} setUserFavorites={setUserFavorites} userFavorites={userFavorites} />
              )}
            <Footer/>
        </div>
    );
}

export default FavoriteList;