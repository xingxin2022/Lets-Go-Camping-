import React, { useState } from 'react';
import styles from './FriendList.module.css';
import dummyUserData from "../../pages/CompareList/dummyUserData.json";

function FriendList() {
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [favoriteParksDisplay, setFavoriteParksDisplay] = useState('Please select users to compare.');

    const toggleUserSelection = (email) => {
        setSelectedUsers(prevSelected =>
            prevSelected.includes(email)
                ? prevSelected.filter(e => e !== email)
                : [...prevSelected, email]
        );
    };

    const handleCompareClick = () => {
        // Reset display message
        setFavoriteParksDisplay('');

        // Find the user data for the selected users
        const selectedUsersData = dummyUserData.filter(user =>
            selectedUsers.includes(user.userEmail)
        );

        // Check if any selected users have a private list
        const hasPrivateList = selectedUsersData.some(user => user.privateList);

        if (hasPrivateList) {
            // If any user has a private list, set an appropriate message
            setFavoriteParksDisplay('One (or more) of the users you selected has a private list.');
        } else {
            // If all selected users have public lists, calculate and show favorite parks
            const selectedParks = selectedUsersData
                .map(user => user.favoritePark)
                .flat();
            const uniqueParks = [...new Set(selectedParks)].join(', ');
            setFavoriteParksDisplay(`Favorite parks: ${uniqueParks}`);
        }
    };

    return (
        <div>
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
                            <h4>{`${user.userEmail} (${user.privateList ? 'private' : 'public'})`}</h4>
                            <p>This user has {user.favoritePark.length} fav parks</p>
                        </div>
                    </div>
                ))}
                <button className={styles.button} onClick={handleCompareClick}> Compare now</button>
            </div>

            <div className={styles.textContainer} data-testid="message-display">
                {favoriteParksDisplay}
            </div>
        </div>
    );
}

export default FriendList;
