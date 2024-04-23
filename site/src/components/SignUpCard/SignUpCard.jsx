import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/Card.module.css';

function SignUpCard() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [responseMessage, setResponseMessage] = useState('');
    const [isErrorMessage, setIsErrorMessage] = useState(false);
    const [showCancelConfirm, setShowCancelConfirm] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setResponseMessage('');
        setIsErrorMessage(false);

        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);
        formData.append('confirmPassword', confirmPassword);

        try {
            const response = await fetch('/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData.toString(),
            });

            if (!response.ok) {
                let message = 'An unknown error occurred.';
                const contentType = response.headers.get('Content-Type');
                if (contentType && contentType.includes('application/json')) {
                    const data = await response.json();
                    message = data.message;
                } else {
                    message = await response.text();
                }
                setResponseMessage(message);
                setIsErrorMessage(true);
            } else {
                const data = await response.json();
                setResponseMessage(data.message);
                setIsErrorMessage(false);
            }
        } catch (error) {
            setResponseMessage('Failed to send request. Please try again later.');
            setIsErrorMessage(true);
        }
    };

    const handleCancel = () => {
        setShowCancelConfirm(true);
    };

    const confirmCancel = () => {
        navigate('/');  // Adjust this as necessary to navigate to the correct route
    };

    return (
        <div className={styles.card} data-testid="signUpCard">
            <h2>Sign Up</h2>
            <form onSubmit={handleSubmit}>
                <div className={styles.cardContent}>
                    <div className={styles.formGroup}>
                        <label htmlFor="username">Name</label>
                        <input
                            type="text"
                            id="usernameRegis"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            data-testid="signup-username"
                        />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="passwordRegis"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            data-testid="signup-password"
                        />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        <input
                            type="password"
                            id="confirmPasswordRegis"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            data-testid="signup-password-confirm"
                        />
                    </div>
                </div>
                <button type="submit">Sign Up</button>
                <button type="button" onClick={handleCancel} className={styles.cancelButton}>Cancel</button>

                <br></br>
                <br></br>

                {showCancelConfirm && (
                    <>
                        <button onClick={confirmCancel} className={styles.confirmButton}>Confirm Cancel</button>
                        <button onClick={() => setShowCancelConfirm(false)} className={styles.denyButton}>Keep Signing
                            Up
                        </button>
                    </>
                )}

                {responseMessage && (
                    <div id="response" className={styles.responseMessage}
                         style={{color: isErrorMessage ? 'red' : 'green'}}>
                        {responseMessage}
                    </div>
                )}
            </form>

            <br />

            <div>
                Have an account already?
                <span onClick={() => navigate('/')} style={{ color: '#BF754B', cursor: 'pointer' }}> Back to Log-in</span>
            </div>
        </div>
    );
}

export default SignUpCard;
