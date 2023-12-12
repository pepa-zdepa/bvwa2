import React, { useState, useEffect } from 'react';
import { useParams, useHistory } from 'react-router-dom';
import API_URLS from './config';

function MessageDetail() {
  const { messageId } = useParams();
  const [message, setMessage] = useState(null);
  const history = useHistory();

  useEffect(() => {
    const fetchMessage = async () => {
      try {
        const response = await fetch(`${API_URLS.GET_MESSAGE_BY_ID.replace('{messageId}', messageId)}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${loggedInUser.token}`,
          },
        });

        if (response.ok) {
          const messageData = await response.json();
          setMessage(messageData);
        } else {
          // Handle error, e.g., invalid token or message not found
          console.error('Error fetching message:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching message:', error);
      }
    };
    fetchMessage();
  }, [messageId, loggedInUser.token]);

  const markAsSeen = async () => {
    try {
      await fetch(`${API_URLS.MARK_MESSAGE_AS_SEEN.replace('{messageId}', messageId)}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${loggedInUser.token}`,
        },
      });

      // Optionally, update the local state or perform other actions

      // Redirect the user to the messages page or handle navigation as needed
      // Example: history.push('/messages');
    } catch (error) {
      console.error('Error marking message as seen:', error);
    }
  };

  return (
    <div className="bg-gradient-to-r from-purple-500 to-orange-500 min-h-screen flex justify-center items-center text-white">
      {message ? (
        <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
          <h2 className="text-3xl font-bold text-center mb-4">Detail zprávy</h2>
          <p>Předmět: {message.subject}</p>
          <p>Od: {message.from}</p>
          <p>Komu: {message.to}</p>
          <p>Zpráva: {message.message}</p>
          <p>Čas: {message.time}</p>
          <p>Odpověď na: {message.responseTo}</p>
          <p>Přečteno: {message.seen ? 'Ano' : 'Ne'}</p>

          {!message.seen && (
            <button onClick={markAsSeen} className="bg-blue-500 hover:bg-blue-600 text-white px-2 py-1 rounded-full mt-4">
              Označit jako přečteno
            </button>
          )}
        </div>
      ) : (
        <p>Loading message...</p>
      )}
    </div>
  );
}

export default MessageDetail;
