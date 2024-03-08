import React, {useState} from "react";
import styles from "../styles/Card.module.css"

function SignUpCard() {

    // State hooks for form inputs and response message
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [responseMessage, setResponseMessage] = useState('');

    // Handle form submission
    const handleSubmit = async (event) => {
        event.preventDefault(); // Prevent default form submission behavior

        // Perform validation if necessary (e.g., check if passwords match)

        // Construct the form data
        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);
        formData.append('confirmPassword', confirmPassword);

        // Make the POST request to the /register endpoint
        try {
            const response = await fetch('/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData.toString(),
            });

            const data = await response.json();
            // Set the response message to display on the page
            console.log(data);
            console.log(data.message);
            alert(data.message);
            setResponseMessage(data.message);

            // Clear the form inputs
            setUsername('');
            setPassword('');
            setConfirmPassword('');
        } catch (error) {
            console.error('Error during the registration process:', error);
            setResponseMessage('An error occurred. Please try again.');
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
            </form>
        </div>
    );
}

export default SignUpCard;