import React, { useState, useEffect } from 'react';

// Recursive component to render messages and their nested responses
const MessageThread = ({ messageId, messages }) => {
    const findOriginalMessage = (msg) => {
        if (!msg) {
            return null; // Return null if the message is undefined
        }
        if (!msg.responseTo) {
            return msg; // Return the current message if it has no responseTo
        }
        const parentMessage = messages.find(parentMsg => parentMsg.id === msg.responseTo);
        return findOriginalMessage(parentMessage);
    };

    const message = messages.find(msg => msg.id === messageId);
    const responses = messages.filter(msg => msg.responseTo === messageId);

    if (!message) {
        return null;
    }

    const originalMessage = findOriginalMessage(message);

    return (
        <div>
            {originalMessage && (
                <div className="mb-6 border-l-2 border-gray-200 pl-4">
                    <Message key={originalMessage.id} message={originalMessage} />
                </div>
            )}
            <Message key={message.id} message={message} />
            {responses.length > 0 && (
                <div className="ml-6 border-l-2 border-gray-200">
                    {responses.map(response => (
                        <MessageThread key={`${message.id}-${response.id}`} messageId={response.id} messages={messages} />
                    ))}
                </div>
            )}
        </div>
    );
};

const Message = ({ message }) => {
    return (
        <div className="mb-2 p-4 bg-white shadow rounded-lg">
            <div className="text-purple-800 text-2xl">{message.from}</div>
            <div className="text-gray-800 text-1xl">{message.subject}</div>
            {message.userFriendlyId && (
                <div className="text-gray-500">{message.userFriendlyId}</div>
            )}
            <div className="text-gray-500">{message.time}</div>
            <div className="text-gray-700 pt-4">{message.message}</div>
        </div>
    );
};

// Main app component
const MailPage = () => {
    const [selectedThreadId, setSelectedThreadId] = useState(null);
    const [currentView, setCurrentView] = useState('doručená');
    const [notification, setNotification] = useState({
        message: '',
        type: '',
    });
    const [replyContent, setReplyContent] = useState('');
    const [showCompose, setShowCompose] = useState(false);
    const [newMessage, setNewMessage] = useState({
        to: '',
        subject: '',
        message: '',
    });
    const [messageState, setMessageState] = useState([]);

    useEffect(() => {
        fetchMessages();
    }, [currentView]);

    const fetchMessages = async () => {
        const direction = currentView === 'odeslaná' ? 'out' : 'in';
        const url = `https://127.0.0.1:8443/user/messages?direction=${direction}`;

        try {
            const response = await fetch(url, { credentials: "include" });
            if (response.ok) {
                const data = await response.json();
                setMessageState(data);
            } else {
                console.error('Failed to fetch messages:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching messages:', error);
        }
    };

    const showNotification = (message, type) => {
        setNotification({ message, type });

        setTimeout(() => {
            hideNotification();
        }, 5000);
    };

    const hideNotification = () => {
        setNotification({ message: '', type: '' });
    };


    // Function to handle sending a new message
    const handleSend = async () => {
        // Check if any of the required fields are empty
        if (!newMessage.to || !newMessage.subject || !newMessage.message) {
            // Show a notification for missing fields
            showNotification('Prosím vyplň všechny pole.', 'error');
            return;
        }

        const messageData = {
            ...newMessage,
            responseTo: null,
        };

        try {
            const response = await fetch('https://127.0.0.1:8443/user/messages', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(messageData),
                credentials: "include"
            });

            if (response.ok) {
                setReplyContent('');
                fetchMessages();
                // Show a success notification
                showNotification('Zpráva byla poslána úspěšně', 'success');
            } else {
                console.error('Failed to send message:', response.statusText);
                // Show an error notification
                showNotification('Zpráva nebyla možná odeslat.', 'error');
            }
        } catch (error) {
            console.error('Error sending message:', error);
            // Show an error notification
            showNotification('An error occurred while sending the message. Please check your network connection.', 'error');
        }
    };



    const handleReply = async () => {
        const messageToReplyTo = messageState.find((msg) => msg.id === selectedThreadId);
        if (messageToReplyTo) {
            const replyData = {
                to: messageToReplyTo.from,
                subject: `Re: ${messageToReplyTo.subject}`,
                message: replyContent,
                responseTo: messageToReplyTo.id,
            };

            try {
                const response = await fetch('https://127.0.0.1:8443/user/messages', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(replyData),
                    credentials: "include"
                });

                if (response.ok) {
                    setReplyContent('');
                    fetchMessages();
                    // Show a success notification for reply
                    showNotification('Zpráva byla poslána úspěšně', 'success');
                } else {
                    console.error('Failed to send message:', response.statusText);
                    // Show an error notification
                    showNotification('Failed to send reply', 'error');
                }
            } catch (error) {
                console.error('Error sending message:', error);
                // Show an error notification
                showNotification('An error occurred while sending the reply. Please check your network connection.', 'error');
            }
        }
    };

    return (
        <div className="flex h-screen">
            {/* Sidebar */}
            <div className="w-1/4 lg:w-1/5 xl:w-1/6 overflow-y-auto border-r border-gray-300">
                <div className="p-3 flex flex-col justify-between lg:flex-row lg:justify-between">
                    <button
                        className={`font-bold ${currentView === 'doručená' ? 'text-purple-700' : 'text-gray-500'} mb-2 lg:mb-0 lg:mr-2`}
                        onClick={() => setCurrentView('doručená')}
                    >
                        Doručená
                    </button>
                    <button
                        className={`font-bold ${currentView === 'odeslaná' ? 'text-purple-700' : 'text-gray-500'} mb-2 lg:mb-0`}
                        onClick={() => setCurrentView('odeslaná')}
                    >
                        Odeslaná
                    </button>
                    <button
                        className="bg-purple-500 hover:bg-grey-500 text-white font-bold py-2 px-4 rounded-full text-2xl lg:text-3xl"
                        onClick={() => setShowCompose(!showCompose)}
                    >
                        +
                    </button>
                </div>
                {messageState.map((message) => (
                    <div
                        key={message.id}
                        onClick={() => setSelectedThreadId(message.id)}
                        className={`cursor-pointer p-3 ${selectedThreadId === message.id ? 'bg-blue-100' : 'hover:bg-gray-100'}`}
                    >
                        {message.userFriendlyId || message.subject}
                    </div>
                ))}
            </div>

            {/* Main content */}
            <div className="w-3/4 p-6 overflow-y-auto">
                {selectedThreadId && (
                    <MessageThread messageId={selectedThreadId} messages={messageState} />
                )}
                {/* Reply section */}
                {currentView === 'doručená' && selectedThreadId !== null && (
                    <div className="w-3/4 p-6 overflow-y-auto">
                        <textarea
                            className="w-full p-2 border rounded"
                            value={replyContent}
                            onChange={(e) => setReplyContent(e.target.value)}
                            placeholder="Write your reply..."
                        />
                        <button
                            className="mt-2 bg-purple-500 hover:bg-purple-600 text-white font-bold py-2 px-4 rounded"
                            onClick={handleReply}
                        >
                            Odesla odpověď
                        </button>
                    </div>
                )}
                {/* Notification */}
                {notification.message && (
                    <div className={`bg-${notification.type === 'error' ? 'red' : 'green'}-500 text-white p-2 rounded mt-2`}>
                        {notification.message}
                    </div>
                )}
            </div>

            {/* Compose form */}
            {showCompose && (
                <div className="lg:w-1/4 p-6 bg-white shadow-lg rounded-lg">
                    <input
                        type="text"
                        className="w-full lg:w-2/3 p-2 m-2 border rounded"
                        placeholder="To"
                        value={newMessage.to}
                        onChange={(e) => setNewMessage({ ...newMessage, to: e.target.value })}
                    />
                    <input
                        type="text"
                        className="w-full lg:w-2/3 p-2 m-2 border rounded"
                        placeholder="Subject"
                        value={newMessage.subject}
                        onChange={(e) => setNewMessage({ ...newMessage, subject: e.target.value })}
                    />
                    <textarea
                        className="w-full lg:w-2/3 p-2 m-2 border rounded"
                        placeholder="Message"
                        value={newMessage.message}
                        onChange={(e) => setNewMessage({ ...newMessage, message: e.target.value })}
                    />
                    <div className="flex flex-col justify-start">
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded mb-2"
                            onClick={handleSend}
                        >
                            Odeslat
                        </button>
                        <button
                            className="bg-red-500 hover:bg-red-700 text-white font-bold py-3 px-4 rounded"
                            onClick={() => setShowCompose(false)}
                        >
                            Zrušit
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MailPage;
