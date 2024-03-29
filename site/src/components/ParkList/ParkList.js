import React from 'react';
import Park from '../Park/Park';
import './ParkList.css';

function ParkList({ parks, onSetShowPark, currentUser, setUserFavorites, userFavorites }) {
  const uniqueParks = Array.from(
      new Map(parks.map((park) => [park.id, park])).values()
    );
    return (
      <div className="parks-container">
        {uniqueParks.map((park) => (
          <Park park={park} key={park.id} onSetShowPark = {onSetShowPark} currentUser={currentUser} setUserFavorites={setUserFavorites} userFavorites={userFavorites} />
        ))}
      </div>
    );
}

export default ParkList;
