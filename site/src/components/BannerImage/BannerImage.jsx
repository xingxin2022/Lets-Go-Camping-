import React , {useState} from "react";
import styles from './BannerImage.module.css'; // CSS Module

function BannerImage() {
    const [isHovered, setIsHovered] = useState(false);

    return (
        <div
            className={styles.banner}
            data-testid="bannerImage"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <img
                src="/images/bannerImageTemporary_0304.png"
                alt="Banner Image of National Park"
                className={`${styles.bannerImage} ${isHovered ? styles.dim : ''}`}
            />
            {isHovered && <div className={styles.overlayText}> Discover Your Dream Park Today. </div>}
        </div>
    );
}

export default BannerImage;