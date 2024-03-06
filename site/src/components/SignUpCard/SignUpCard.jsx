import React from "react";
import styles from "../styles/Card.module.css"

function SignUpCard() {
    return (
        <div className={styles.card} data-testid="signUpCard">
            <h2>Sign Up</h2>
            <form>
                <div className={styles.cardContent}>
                    <div className={styles.formGroup}>
                        <label htmlFor="username">Name</label>
                        <input type="text" id="usernameRegis"/>
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="password">Password</label>
                        <input type="password" id="passwordRegis"/>
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        <input type="password" id="confirmPasswordRegis"/>
                    </div>
                </div>
                <button type="submit">Sign Up</button>
            </form>
        </div>
    );
}

export default SignUpCard;