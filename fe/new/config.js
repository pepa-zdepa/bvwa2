const API_BASE_URL = 'https://127.0.0.1:8443';

export const API_URLS = {
  LOGIN: `${API_BASE_URL}/auth/login`,
  LOGOUT: `${API_BASE_URL}/auth/logout`,
  REFRESH: `${API_BASE_URL}/auth/refresh`,
  CHECK_USERNAME: `${API_BASE_URL}/auth/check-username-available`,
  REGISTER_USER: `${API_BASE_URL}/user`,
  GET_USER: `${API_BASE_URL}/user`,
  UPDATE_USER: `${API_BASE_URL}/user`,
  UPDATE_PASSWORD: `${API_BASE_URL}/user/update-password`,
  UPLOAD_IMAGE: `${API_BASE_URL}/user/upload-image`,
  GET_IMAGE: `${API_BASE_URL}/user/{id}/image`,  // Replace {id} with the actual user ID
  GET_GENDERS: `${API_BASE_URL}/user/genders`,
  GET_ROLES: `${API_BASE_URL}/user/roles`,
  GET_USER_MESSAGES: `${API_BASE_URL}/user/messages`,
  SEND_MESSAGE: `${API_BASE_URL}/user/messages`,
  GET_MESSAGE_BY_ID: `${API_BASE_URL}/user/messages/{messageId}`,  // Replace {messageId} with the actual message ID
  MARK_MESSAGE_AS_SEEN: `${API_BASE_URL}/user/messages/{messageId}/seen`,  // Replace {messageId} with the actual message ID
  GET_UNREAD_MESSAGES: `${API_BASE_URL}/user/messages/unread`,
  GET_ADMIN_USER: `${API_BASE_URL}/admin/user/{id}`,  // Replace {id} with the actual user ID
  UPDATE_ADMIN_USER: `${API_BASE_URL}/admin/user/{id}`,  // Replace {id} with the actual user ID
  UPDATE_ADMIN_USER_PASSWORD: `${API_BASE_URL}/admin/user/{id}/password`,  // Replace {id} with the actual user ID
  UPDATE_ADMIN_USER_ROLE: `${API_BASE_URL}/admin/user/{id}/role`,  // Replace {id} with the actual user ID
  DELETE_ADMIN_USER: `${API_BASE_URL}/admin/user/{id}`,  // Replace {id} with the actual user ID
  GET_ALL_ADMIN_USERS: `${API_BASE_URL}/admin/users`,
};

export default API_URLS;
