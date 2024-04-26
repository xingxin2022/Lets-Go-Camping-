import React from "react";

import { useNavigate } from "react-router-dom";
import { useUser } from '../../UserContext';
import styles from './Header.module.css';

function Header() {
    const { currentUser, setCurrentUser } = useUser();

    const navigate = useNavigate(); // Initialize useNavigate hook
    const navigateToSearch = () => navigate('/search'); // Function to navigate to the search page
    const navigateToHome = () => navigate('/'); //
    const navigateToFavorite = () => navigate('/favorite');
    const navigateToCompare = () => navigate('/compare');

    const handleLogout = () => {
        fetch('/api/users/logout', {
            method: 'POST',
            credentials: 'include'
        })
        .then(() => {
            setCurrentUser(null);
            navigate('/');
        });
    };


    return (
        <header className={styles.header} data-testid="header">
            <img src="/images/logoTemporary_0302.png" alt="Let's Go Camping!" className={styles.logo}/>

            <h1 className={styles.title}>Let's Go Camping!</h1>
            <nav>
                <ul className={styles.navList}>
                    <li className={styles.navItem} onClick={navigateToHome} data-testid="toHome">Home</li>
                    {currentUser && <li className={styles.navItem} onClick={navigateToSearch} data-testid="toSearch">Search</li>}
                    {currentUser && <li className={styles.navItem} onClick={navigateToFavorite} data-testid="toSuggest">FavoriteList</li>}
                    {currentUser && <li className={styles.navItem} onClick={navigateToCompare} data-testid="toSuggest">CompareList</li>}
                    {currentUser && <li className={styles.navItem} onClick={handleLogout}>LogOut</li>}
                </ul>
            </nav>
        </header>
    );
}

export default Header;
