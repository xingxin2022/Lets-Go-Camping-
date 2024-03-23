import React from "react";
import styles from './Footer.module.css';

function Footer() {
    return (
        <header className={styles.footer} data-testid="footer">
                <div className="footer">
                    © {new Date().getFullYear()} USC CSCI310 Team 38. All Rights Reserved. | Developed by the ELiu, XGuo, SYang, ALiu, DYu | Contact Us: dyu18149@usc.edu
                </div>
        </header>
    );
}

export default Footer;
