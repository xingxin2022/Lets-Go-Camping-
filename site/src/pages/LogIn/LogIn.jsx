import React, {/* useState */} from "react";
// import { useNavigate } from "react-router-dom";

import Header from "../../components/Header/Header";
import BannerImage from"../../components/BannerImage/BannerImage";
import SignUpCard from "../../components/SignUpCard/SignUpCard";
import LogInCard from "../../components/LogInCard/LogInCard";
import Footer from "../../components/Footer/Footer";

import styles from "./LogIn.module.css";

function LogIn() {
    return (
        <div className={styles.logInPage}>
            <Header />
            <BannerImage />
            <div className={styles.cardsContainer}>
                <SignUpCard />
                <LogInCard />
            </div>
            <Footer />
        </div>
    );
}

export default LogIn;