import React, { lazy, Suspense } from "react"
import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"

import Login from "./pages/Login.jsx"
import "./index.css"
import Dashboard from "./pages/Dashboard.jsx"
import Providers from "./utils/Providers.jsx"

const ChangeRequestPage = lazy(() => import("./pages/ChangeRequestPage"))

ReactDOM.createRoot(document.getElementById("root")).render(
	<React.StrictMode>
		<Providers>
			<BrowserRouter>
				<Suspense fallback={<div>Loading...</div>}>
					<Routes>
						<Route path="/" element={<Login />} />
						<Route path="/dashboard" element={<Dashboard />} />
						<Route path="/change-request/:id" element={<ChangeRequestPage />} />
					</Routes>
				</Suspense>
			</BrowserRouter>
		</Providers>
	</React.StrictMode>
)
