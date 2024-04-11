import React, { /* useState */ } from "react";

import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import FriendList from "../../components/FriendList/FriendList";

import styles from "./CompareList.module.css";

function CompareList() {
    return (
        <div className={styles.comparePage}>
            <Header />
            <FriendList />
            <Footer />
        </div>
    );
}

export default CompareList;