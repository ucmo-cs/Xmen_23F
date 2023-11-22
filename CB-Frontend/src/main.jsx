import React from "react"
import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"

import Login from "./pages/Login.jsx"
import "./index.css"
import Dashboard from "./pages/Dashboard.jsx"
import Providers from "./utils/Providers.jsx"
import ChangeRequestPage from "./pages/ChangeRequestPage.jsx"
import Register from "./pages/Register.jsx"
import ABCD from "./pages/ChangeRequestPage.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(
	<React.StrictMode>
		<Providers>
			<BrowserRouter>
				<Routes>
					<Route path="/" element={<Login />} />
					<Route path="/dashboard" element={<Dashboard />} />
					<Route path="/change-request" element={<ABCD />} />
					<Route path="/register" element={<Register />} />
				</Routes>
			</BrowserRouter>
		</Providers>
	</React.StrictMode>
)
