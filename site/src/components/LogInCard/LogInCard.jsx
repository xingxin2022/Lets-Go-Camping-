import React from "react";
import styles from "../styles/Card.module.css"

function LogInCard() {
    return (
        <div className={styles.card} data-testid="logInCard">
            <h2>Log In</h2>
            <form>
                <div className={styles.formGroup}>
                    <label htmlFor="username">Name</label>
                    <input type="text" id="usernameLogin"/>
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="password">Password</label>
                    <input type="password" id="passwordLogin"/>
                </div>
                <button type="submit">Log In</button>
            </form>
        </div>
    );
}

export default LogInCard;