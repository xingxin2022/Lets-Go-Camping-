import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import Modal from 'react-modal';
import PopUpModal from './PopUpModal';

// Mock Modal to prevent it from throwing errors about app element
Modal.setAppElement(document.createElement('div'));

describe('PopUpModal', () => {
  const mockCloseModal = jest.fn();
  const parkMock = {
    fullName: 'Test Park',
    images: [{ url: 'test.jpg', altText: 'Test Image' }],
    addresses: [{ line1: '123 Test St', city: 'Testville', stateCode: 'TS', countryCode: 'USA' }],
    url: 'https://test.com',
    entranceFees: [{ cost: '10.00' }],
    description: 'Test Description',
    activities: [{ name: 'Hiking' }, { name: 'Swimming' }],
    isFavorite: true,
  };

//  test('renders PopUpModal correctly', () => {
//    render(<PopUpModal modalIsOpen={true} closeModal={mockCloseModal} park={parkMock} />);
//
//    expect(screen.getByText('Test Park 🌟️')).toBeInTheDocument();
//    expect(screen.getByAltText('Test Image')).toHaveAttribute('src', 'test.jpg');
//    expect(screen.getByText('123 Test St, Testville, TS, USA')).toBeInTheDocument();
//    expect(screen.getByText('https://test.com')).toBeInTheDocument();
//    expect(screen.getByText('$10.00')).toBeInTheDocument();
//    expect(screen.getByText('Test Description')).toBeInTheDocument();
//    expect(screen.getByText('Hiking, Swimming')).toBeInTheDocument();
//  });

  test('closes modal on button click', () => {
    render(<PopUpModal modalIsOpen={true} closeModal={mockCloseModal} park={parkMock} />);
    fireEvent.click(screen.getByText('close'));
    expect(mockCloseModal).toHaveBeenCalled();
  });


});
