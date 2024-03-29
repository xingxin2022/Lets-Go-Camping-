import React from "react";

import { useNavigate } from "react-router-dom"; // Import useNavigate
import styles from './Header.module.css';

function Header() {

    const navigate = useNavigate(); // Initialize useNavigate hook

    const navigateToSearch = () => navigate('/search'); // Function to navigate to the search page
    const navigateToHome = () => navigate('/'); //

    return (
        <header className={styles.header} data-testid="header">
            <img src="/images/logoTemporary_0302.png" alt="Let's Go Camping!" className={styles.logo}/>

            <h1 className={styles.title}>Let's Go Camping!</h1>
            <nav>
                <ul className={styles.navList}>
                    <li className={styles.navItem} onClick={navigateToHome} data-testid="toHome">Home</li>
                    <li className={styles.navItem} onClick={navigateToSearch} data-testid="toSearch">Search</li>
                    <li className={styles.navItem}>Suggest</li>
                    <li className={styles.navItem}>Contact</li>
                    <li className={styles.navItem}>Example</li>
                </ul>
            </nav>
        </header>
    );
}

export default Header;
