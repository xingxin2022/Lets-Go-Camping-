import React from "react";
import styles from './Header.module.css';

function Header() {
    return (
        <header className={styles.header} data-testid="header">
            <img src="/images/logoTemporary_0302.png" alt="Let's Go Camping!" className={styles.logo}/>
            <h1 className={styles.title}>Let's Go Camping!</h1>
            <nav>
                <ul className={styles.navList}>
                    <li className={styles.navItem}>Log-In</li>
                    <li className={styles.navItem}>Search</li>
                    <li className={styles.navItem}>Suggest</li>
                    <li className={styles.navItem}>Contact</li>
                    <li className={styles.navItem}>Example</li>
                </ul>
            </nav>
        </header>
    );
}

export default Header;
