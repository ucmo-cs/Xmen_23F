import axios from "axios"
import { useParams } from "react-router-dom"
import apiFetch from "../utils/apiFetch"
import { useQuery } from "@tanstack/react-query"
import dayjs from "dayjs"
import NavBar from "../components/NavBar"
function ChangeRequestPage() {
	const params = useParams()
	const { isError, isLoading, data } = useQuery({
		queryKey: ["change-request"],
		queryFn: async () =>
			await apiFetch("GET", `/api/v1/change/${params.id}`)
				.then(res => res.data)
				.catch(err => console.log(err)),
	})

	if (isLoading) return <p>Loading...</p>

	if (isError) return <p>Error...</p>

	console.log(data)

	return (
		<div>
			<NavBar />
			<div className="flex items-center justify-center p-4  mx-10 ">
				<form className="">
					<div className="my-10">
						<label htmlFor="changeRequestId">Change Request ID:</label>
						<input type="text" id="changeRequestId" value={data.id} disabled />
					</div>
					<div className="my-10">
						<label htmlFor="applicationId">Application ID:</label>
						<input
							type="text"
							id="applicationId"
							value={data.applicationId}
							disabled
						/>
					</div>
					<div className="my-10">
						<label htmlFor="changeType">Change Type:</label>
						<input
							type="text"
							id="changeType"
							value={data.changeType}
							disabled
						/>
					</div>
					<div>
						<label htmlFor="dateCreated">Date Created:</label>
						<input
							type="text"
							id="dateCreated"
							value={dayjs(data.dateCreated).format("MM/DD/YYYY")}
							disabled
						/>
					</div>
					<div>
						<label htmlFor="dateUpdated">Date Updated:</label>
						<input
							type="text"
							id="dateUpdated"
							value={dayjs(data.dateUpdated).format("MM/DD/YYYY")}
							disabled
						/>
					</div>
				</form>
			</div>
		</div>
	)
}

export default ChangeRequestPage
