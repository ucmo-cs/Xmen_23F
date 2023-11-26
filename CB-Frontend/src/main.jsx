import React from "react"
import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"

import Login from "./pages/Login.jsx"
import "./index.css"
import Dashboard from "./pages/Dashboard.jsx"
import Providers from "./utils/Providers.jsx"
import Register from "./pages/Register.jsx"
import ABCD from "./pages/ChangeRequestPage.jsx"
import GrabChangeRequest from "./pages/EditChangeRequestPage.jsx"

ReactDOM.createRoot(document.getElementById("root")).render(
	<React.StrictMode>
		<Providers>
			<BrowserRouter>
				<Routes>
					<Route path="/" element={<Login />} />
					<Route path="/dashboard" element={<Dashboard />} />
					<Route path="/change-request" element={<ABCD />} />
					<Route path="/register" element={<Register />} />
					<Route path="/login" element={<Login />} />
					<Route path="/change-request/:id" element={<GrabChangeRequest />} />
					<Route path="*" element={<h1>404</h1>} />
				</Routes>
			</BrowserRouter>
		</Providers>
	</React.StrictMode>
)
