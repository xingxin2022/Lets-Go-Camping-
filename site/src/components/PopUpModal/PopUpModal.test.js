import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import PopUpModal from './PopUpModal';
import Modal from 'react-modal';

Modal.setAppElement = jest.fn();

const mockHandleClick = jest.fn();
const mockCloseModal = jest.fn();

const parkMock = {
    fullName: 'Yosemite National Park',
    isFavorite: true,
    images: [{ url: 'test_image_url', altText: 'test_image_alt' }],
    addresses: [{ line1: '123 Main St', city: 'Park City', stateCode: 'PC', countryCode: 'USA' }],
    url: 'https://www.nps.gov/yose',
    entranceFees: [{ cost: '30.00' }],
    description: 'A great place to visit',
    amenities: [{ name: 'Parking' }, { name: 'Restrooms' }],
    activities: [{ name: 'Hiking' }, { name: 'Camping' }],
};

const incompleteParkMock = {
      ...parkMock,
      isFavorite: false,
      images: [],
      addresses: [],
      url: '',
      entranceFees: [],
      description: '',
      amenities: [],
      activities: [],
    };

test('renders the modal with all information', async () => {
    render(
      <PopUpModal
        modalIsOpen={true}
        closeModal={mockCloseModal}
        park={parkMock}
        handleClick={mockHandleClick}
      />
    );

    // Assertions for park details
    expect(screen.getByText(/Yosemite National Park/i)).toBeInTheDocument();
    expect(screen.getByText(/🌟/i)).toBeInTheDocument(); // Favourite icon
    expect(screen.getByRole('img')).toHaveAttribute('src', parkMock.images[0].url);
    expect(screen.getByText(new RegExp(parkMock.addresses[0].line1, 'i'))).toBeInTheDocument();
      expect(screen.getByText(new RegExp(parkMock.entranceFees[0].cost, 'i'))).toBeInTheDocument();
      expect(screen.getByText(new RegExp(parkMock.description, 'i'))).toBeInTheDocument();

    // Assertions for amenities and activities
//    parkMock.amenities.forEach((amenity) => {
//      expect(screen.getByText(amenity.name)).toBeInTheDocument();
//      expect(screen.getByText(/,/i).toBeInTheDocument();
//    });
//    parkMock.activities.forEach((activity) => {
//      expect(screen.getByText(activity.name)).toBeInTheDocument();
//      expect(screen.getByText(/,/i).toBeInTheDocument();
//    });

  if (parkMock.amenities && parkMock.amenities.length > 0) {
    const amenitiesContainer = screen.getByText(/Amenities:/i).nextSibling;
    expect(amenitiesContainer).toHaveTextContent(
      parkMock.amenities.map((amenity, index) =>
        index < parkMock.amenities.length - 1 ? amenity.name + ', ' : amenity.name
      ).join('')
    );
  }

  if (parkMock.activities && parkMock.activities.length > 0) {
    const activitiesContainer = screen.getByText(/Activities:/i).nextSibling;
    expect(activitiesContainer).toHaveTextContent(
      parkMock.activities.map((activity, index) =>
        index < parkMock.activities.length - 1 ? activity.name + ', ' : activity.name
      ).join('')
    );
  }
})

test('renders not applicable for missing information', async () => {
  render(
        <PopUpModal
          modalIsOpen={true}
          closeModal={mockCloseModal}
          park={incompleteParkMock}
          handleClick={mockHandleClick}
        />
      );

      // Assertions for 'Not applicable' texts
      expect(screen.queryByText(/🌟/i)).toBeNull();
        expect(screen.getByText(/Address not available/i)).toBeInTheDocument();
        expect(screen.getByText(/URL not available/i)).toBeInTheDocument();
        expect(screen.getByText(/Entrance fee not available/i)).toBeInTheDocument();
        expect(screen.getByText(/Description not applicable/i)).toBeInTheDocument();

        const notApplicableText = screen.queryByText(/Amenities not applicable/i);
        expect(notApplicableText).toBeInTheDocument;
});


test('renders "Not applicable" when there are no amenities or activities', async () => {
  // Pass the incompleteParkMock which has empty amenities and activities
  render(
    <PopUpModal
      modalIsOpen={true}
      closeModal={mockCloseModal}
      park={incompleteParkMock}
      handleClick={mockHandleClick}
    />
  );

 expect(screen.queryByText(/parking/i)).toBeNull(); // Adjust text based on your actual content
   expect(screen.queryByText(/hiking/i)).toBeNull(); // Adjust text based on your actual content

   await waitFor(() => {
       // Using regular expressions to account for case sensitivity or extra whitespace
       expect(screen.queryByText(/amenities not applicable/i)).toBeInTheDocument();
       expect(screen.queryByText(/activities not applicable/i)).toBeInTheDocument();
     });
});





