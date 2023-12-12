import React from 'react';

function Footer() {
  return (
    <footer className="bg-gray-900 text-white p-4">
      <div className="container mx-auto text-center">
        &copy; {new Date().getFullYear()} Dep dep website. Všechno je náše.
      </div>
    </footer>
  );
}

export default Footer;