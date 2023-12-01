import React, { useEffect, useState } from "react"
import { useQuery } from "@tanstack/react-query"
import { Link, useNavigate } from "react-router-dom"
import NavBar from "@/components/NavBar.jsx"
import apiFetch from "../utils/apiFetch"
import dayjs from "dayjs"
import relativeTime from "dayjs/plugin/relativeTime"
import LoadSpinner from "../components/LoadSpinner"
dayjs.extend(relativeTime)

function Dashboard() {
	const navigate = useNavigate()
	const [page, setPage] = useState(0)
	const [size, setSize] = useState(5)
	const [showAuthorUsername, setShowAuthorUsername] = useState(true)
	const [state, setState] = useState("Application")

	const fetchData = async (
		page = 0,
		size = 5,
		showAuthorUsername = true,
		state = "Application"
	) => {
		try {
			const res = await apiFetch(
				"GET",
				`/api/v1/change?page=${page}&size=${size}&showAuthorUsername=${showAuthorUsername}&state=${state}`
			)
			console.log(res)
			return res
		} catch (err) {
			console.log(err)
		}
	}

	const { data, isError, isLoading } = useQuery({
		queryKey: ["changeRequest", state, page, size, showAuthorUsername],
		queryFn: async () => {
			const res = await fetchData(page, size, showAuthorUsername, state)
			console.log(res.data)
			return res.data.content
		},
		keepPreviousData: true,
	})

	return (
		//	https://flowbite.com/docs/components/tables/
		<div>
			<NavBar />
			{isError && <div>Something went wrong ...</div>}

			<div className="flex flex-col m-10 sm: mx-4 opacity-75">
				<div className="m-6 p-4 relative overflow-x-auto shadow-md sm:rounded-lg bg-slate-300">
					<div className="flex items-center pb-4 gap-5">
						<button
							className="text-white p-2 bg-gray-400"
							onClick={() => {
								setState("Application")
							}}
							disabled={state === "Application"}>
							Application
						</button>
						<button
							className="text-white p-2 bg-gray-400"
							onClick={() => {
								setState("Department")
							}}
							disabled={state === "Department"}>
							Department
						</button>
						<button
							className="text-white p-2 bg-gray-400 "
							onClick={() => {
								setState("Complete")
							}}
							disabled={state === "Complete"}>
							Complete
						</button>
						<button
							className="text-white p-2 bg-gray-400"
							onClick={() => {
								setState("Frozen")
							}}
							disabled={state === "Frozen"}>
							Frozen
						</button>
					</div>

					{isLoading ? (
						<LoadSpinner />
					) : (
						<div className="inline-block min-w-full">
							<div className="overflow-hidden">
								<table className="w-full text-sm text-left text-gray-50">
									<thead className="text-xs text-gray-700 uppercase bg-gray-50 ">
										<tr className=" transition duration-200 ease-in-out">
											<th
												scope="col"
												className="px-6 py-3 hover:bg-neutral-300 transition duration-300 ease-in-out">
												Id
											</th>
											<th
												scope="col"
												className="px-6 py-3 hover:bg-neutral-300 transition duration-300 ease-in-out">
												Application Id
											</th>
											<th
												className="px-6 py-3 hover:bg-neutral-300 transition duration-300 ease-in-out"
												scope="col">
												State
											</th>
											<th className="px-6 py-3 hover:bg-neutral-300 transition duration-300 ease-in-out">
												Role Type
											</th>

											<th
												scope="col"
												className="px-6 py-3 hover:bg-neutral-300 transition duration-300 ease-in-out">
												Change Type
											</th>
											<th
												scope="col"
												className="px-6 py-3 hover:bg-neutral-300 transition duration-300 ease-in-out">
												Date
											</th>
										</tr>
									</thead>
									<tbody className="bg-white ">
										{data.map(change => (
											<tr
												className="bg-white"
												key={change.id}
												onClick={() => {
													navigate(`/change-request/${change.id}`)
												}}>
												<th
													scope="row"
													className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
													{change.id}
												</th>
												<td className="px-6 py-4 text-black">
													{change.applicationId}
												</td>
												<td className="px-6 py-4 text-black">{change.state}</td>
												<td className="px-6 py-4 text-black">
													{change.roles.name}
												</td>
												<td className="px-6 py-4  text-black">
													{change.changeType}
												</td>
												<td className="px-6 py-4 text-black ">
													{dayjs(change.dateCreated).format("MM/DD/YYYY")}
												</td>
											</tr>
										))}
									</tbody>
								</table>
							</div>
						</div>
					)}
				</div>
			</div>
		</div>
	)
}

export default Dashboard
