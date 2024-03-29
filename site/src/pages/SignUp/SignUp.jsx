import React, {/* useState */} from "react";
// import { useNavigate } from "react-router-dom";

import Header from "../../components/Header/Header";
import BannerImage from"../../components/BannerImage/BannerImage";
import SignUpCard from "../../components/SignUpCard/SignUpCard";
import Footer from "../../components/Footer/Footer";

import styles from "./SignUp.module.css";

function SignUp() {
    return (
        <div className={styles.signUpPage}>
            <Header />
            <BannerImage />
            <div className={styles.cardsContainer}>
                <SignUpCard/>
            </div>
            <Footer/>
        </div>
    );
}

export default SignUp;