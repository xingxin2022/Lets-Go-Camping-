

import React, { useState } from 'react';
import styles from './FriendList.module.css';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import { useUser } from '../../UserContext';
import dummyUserData from "../../pages/CompareList/dummyUserData.json";

function FriendList() {
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [favoriteParksDisplay, setFavoriteParksDisplay] = useState([]);
    const { currentUser, setCurrentUser } = useUser();
    const [modalIsOpen, setIsOpen] = useState(false);

    const toggleUserSelection = (email) => {
        setSelectedUsers(prevSelected =>
            prevSelected.includes(email)
                ? prevSelected.filter(e => e !== email)
                : [...prevSelected, email]
        );
    };

    const handleCompareClick = () => {
        if (selectedUsers.length === 0) {
            setFavoriteParksDisplay('Please select users to compare.');
            return;
        }
        // Reset display message
        setFavoriteParksDisplay('');

        // Find the user data for the selected users
        const selectedUsersData = dummyUserData.filter(user =>
            selectedUsers.includes(user.userEmail)
        );

        let parksInfo = {};
        // Total number of selected users to calculate ratio
        const totalUsers = selectedUsers.length;
        // Check if any selected users have a private list
        const hasPrivateList = selectedUsersData.some(user => user.privateList);

        if (hasPrivateList) {
            // If any user has a private list, set an appropriate message
            setFavoriteParksDisplay('One (or more) of the users you selected has a private list.');
        } else {

            selectedUsersData.forEach(user => {
                user.favoritePark.forEach(park => {
                    if (!parksInfo[park]) {
                        parksInfo[park] = {
                            parkName: park,
                            ratio: 1 / totalUsers,
                            userList: [user.userEmail]
                        };
                    } else {
                        parksInfo[park].ratio += 1 / totalUsers;
                        parksInfo[park].userList.push(user.userEmail);
                    }
                });
            });
            // Construct and sort components for each park with tooltips
            const parksComponents = Object.values(parksInfo)
                .sort((a, b) => b.ratio - a.ratio)  // Sorting parks by descending ratio
                .map((park, index) => (
                    <div key={index} className={styles.parkItem}>
                        {park.parkName}:
                        <span className={styles.ratio}>
                                  {park.ratio.toFixed(2)}
                            <div className={styles.tooltip}>
                                 Users: {park.userList.join(', ')}
                             </div>
                          </span>
                    </div>
                ));


            setFavoriteParksDisplay(parksComponents);

        }
    };
    return (
        <div className={styles.friendListContainer}>
            {dummyUserData.map((user, index) => (
                <div
                    key={index}
                    data-testid={`user-${user.userEmail}`}
                    className={`${styles.friendItem} ${selectedUsers.includes(user.userEmail) ? styles.active : ''}`}
                    onClick={() => toggleUserSelection(user.userEmail)}
                >
                    <div className={styles.friendItemIcon}/>
                    <div className={styles.friendItemContent}>
                        <h6>{`${user.userEmail} (${user.privateList ? 'private' : 'public'})`}</h6>
                        <p>This user has {user.favoritePark.length} favorite parks</p>
                    </div>
                    {/* Conditional tooltip content based on user privacy setting */}
                    <div className={styles.tooltip}>
                        {user.privateList
                            ? "This user has a private list"
                            : user.favoritePark.join(', ')}
                    </div>
                </div>
            ))}
            <button className={styles.button} onClick={handleCompareClick}>Compare now</button>
            <div className={styles.textContainer} data-testid="message-display">
                {favoriteParksDisplay}
            </div>
        </div>
    );


}

export default FriendList;

