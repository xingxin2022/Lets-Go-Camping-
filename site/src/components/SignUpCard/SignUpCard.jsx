import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import styles from "../styles/Card.module.css"

function SignUpCard() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [responseMessage, setResponseMessage] = useState('');
    const [isErrorMessage, setIsErrorMessage] = useState(false); // New state to track if the message is an error
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        // Reset states
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
                    // If the content type is JSON, parse it as JSON
                    const data = await response.json();
                    message = data.message;
                } else {
                    // Otherwise, fallback to reading it as text
                    message = await response.text();
                }

                setResponseMessage(message);
                setIsErrorMessage(true);
            } else {
                // If the response is OK, then parse and process the JSON data
                const data = await response.json();
                setResponseMessage(data.message);
                setIsErrorMessage(false);
            }

        } catch (error) {
            //console.error('Error during the registration process:', error);
            setResponseMessage('Failed to send request. Please try again later.');
            setIsErrorMessage(true);
        }
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
                            value={username} // Binds input value to the username state
                            onChange={(e) => setUsername(e.target.value)} // Updates username state on input change
                            data-testid="signup-username"
                        />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="passwordRegis"
                            value={password} // Binds input value to the password state
                            onChange={(e) => setPassword(e.target.value)} // Updates password state on input change
                            data-testid="signup-password"
                        />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        <input
                            type="password"
                            id="confirmPasswordRegis"
                            value={confirmPassword} // Binds input value to the confirmPassword state
                            onChange={(e) => setConfirmPassword(e.target.value)} // Updates confirmPassword state on input change
                            data-testid="signup-password-confirm"
                        />
                    </div>
                </div>
                <button type="submit">Sign Up</button>

                {responseMessage && (
                    <div id="response" className={styles.responseMessage} style={{ color: isErrorMessage ? 'red' : 'green' }}>
                        <br></br>
                        {responseMessage}
                    </div>
                )}

            </form>

            <br></br>

            <div>
            Have an account already?
                <span onClick={() => navigate('/')}
                      style={{color: '#BF754B', cursor: 'pointer'}}> Back to Log-in</span>
            </div>

        </div>
    );
}

export default SignUpCard;