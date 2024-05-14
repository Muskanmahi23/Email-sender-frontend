import { useState } from 'react'
import { Toaster } from 'react-hot-toast'
import EmailSender from './components/EmailSender'

function App() {
    return (
      <>
        <EmailSender />
        <Toaster />
      </>
    )
}

export default App
