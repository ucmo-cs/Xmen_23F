import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Login from './Login.tsx'
import Dashboard from './Dashboard.tsx'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
			<BrowserRouter>
			<Routes>
				<Route path="/" element={<Login />}/>
        <Route path="/dashboard" element={<Dashboard/>}/>
				
			</Routes>
		</BrowserRouter>
  </React.StrictMode>,
)
