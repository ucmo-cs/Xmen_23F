import React from "react"
import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"

import Login from "./pages/Login.jsx"
import "./index.css"
import Dashboard from "./pages/Dashboard.jsx"
import Providers from "./utils/Providers.jsx"

ReactDOM.createRoot(document.getElementById("root")).render(
	<React.StrictMode>
		<Providers>
			<BrowserRouter>
				<Routes>
					<Route path="/" element={<Login />} />
					<Route path="/dashboard" element={<Dashboard />} />
				</Routes>
			</BrowserRouter>
		</Providers>
	</React.StrictMode>
)
