import {SightingDocument} from '@trackrejoice/typescriptmodels';

/*for (let index = 0; index < 10; index++) {
  myArray.push(crypto.randomUUID())
}

function randomDate(start, end) {
  return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
}

for (let index = 0; index < 100; index++) {
  documents.push({'ownerId':{'type':myArray[index%myArray.length]}, 'removeAfterMatching': index % 3 == 0, 'sightingId':{'type':crypto.randomUUID()}, 'timestamp': randomDate(new Date(2025, 1,1), new Date())});
}*/
export const mockUserIds = ['20ce3ace-82cf-413b-9568-51cc014ad753', '8526da30-5964-42da-ac64-9965c7df04d6', '4c1a0d2a-8061-4a7e-b695-90b8b7808b19', 'ba22d920-f201-4f64-ad20-6c3f81cd0ccf', '93ca3a76-6c3b-45cd-8f6f-a51358bf86ff', 'ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350', '26c8526d-3256-46be-afcf-733d544960a9', 'c61e7d9a-f793-48e7-bd09-d9fbf1c4c064', '46e81b01-9de0-447a-b3ef-bb6d5d2156b5', '209b39de-8e96-4076-8958-7b92974299c7'];
export const mockSightings: SightingDocument[] =
  [
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": true,
      "sightingId": "9586ced6-eff4-4caf-a2b3-1c889099db87",
      "timestamp": "2025-07-11T16:21:10.994Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": false,
      "sightingId": "c3c0d7e0-c895-4590-918c-60d3b570a80b",
      "timestamp": "2026-01-01T05:04:59.206Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": false,
      "sightingId": "8f5028a6-8c68-46b4-8053-216ebc1fb236",
      "timestamp": "2025-03-27T22:08:05.980Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": true,
      "sightingId": "548c0d22-d715-4c09-9530-71c5764cb76c",
      "timestamp": "2025-09-03T04:04:47.468Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": false,
      "sightingId": "bb8eb7b1-c4b5-4951-a7f5-4b6f6b45ece6",
      "timestamp": "2025-08-04T23:58:55.989Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": false,
      "sightingId": "41706cd8-62ca-4fb0-880b-309dd5ac4507",
      "timestamp": "2025-08-16T00:25:56.413Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": true,
      "sightingId": "c328b93c-55da-4f86-9e70-aaa08f710393",
      "timestamp": "2025-08-07T11:34:53.669Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": false,
      "sightingId": "ca5797d1-e6d3-43e2-93ce-c63c7acc58b1",
      "timestamp": "2025-10-23T17:26:59.932Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": false,
      "sightingId": "08dc3267-944c-46b6-9128-abe37b762e29",
      "timestamp": "2025-12-15T22:11:40.752Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": true,
      "sightingId": "536a55a2-5af1-4b9b-a239-54a8e20826e5",
      "timestamp": "2025-07-26T10:59:44.226Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": false,
      "sightingId": "6751e057-0343-40fd-82d5-8271cef41539",
      "timestamp": "2025-12-06T01:28:23.407Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": false,
      "sightingId": "aed8677e-691e-4dc6-b80a-0a823b32dd56",
      "timestamp": "2025-02-05T02:53:31.938Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": true,
      "sightingId": "3cdc8b6c-f95a-4f44-a1b2-43a5259c0bb8",
      "timestamp": "2026-01-31T11:10:41.748Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": false,
      "sightingId": "e702004f-7dde-42cd-a8b0-46e3bd0c9da8",
      "timestamp": "2025-04-28T20:31:40.103Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": false,
      "sightingId": "b3dee381-ccc0-4502-9c40-1cf6bd1b76e2",
      "timestamp": "2025-08-23T12:57:02.194Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": true,
      "sightingId": "ceed6d17-5da9-481e-bd72-ef71ec77961d",
      "timestamp": "2025-08-20T17:34:27.144Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": false,
      "sightingId": "77002999-4941-4814-8625-4d498334df7f",
      "timestamp": "2025-06-28T07:01:55.352Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": false,
      "sightingId": "fc4e926a-993a-417d-9b74-c9623806d679",
      "timestamp": "2026-01-23T23:52:34.180Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": true,
      "sightingId": "86f4003f-9188-4d32-9b53-e353e3abc29f",
      "timestamp": "2025-09-25T08:00:22.669Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": false,
      "sightingId": "d623961b-6367-4f97-b443-4a6e1a553c0c",
      "timestamp": "2025-02-07T23:12:42.348Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": false,
      "sightingId": "9826f5f7-e0d3-4dd8-b550-56a970c2d3e9",
      "timestamp": "2025-03-26T18:11:28.063Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": true,
      "sightingId": "4e170edd-be83-40f1-86d7-5ab4aa101fcb",
      "timestamp": "2025-04-18T18:17:03.973Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": false,
      "sightingId": "d9d24882-a500-42e1-bff3-19aac95105c0",
      "timestamp": "2025-04-02T11:53:17.653Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": false,
      "sightingId": "e174567b-b744-4b53-be52-91c8b9967b13",
      "timestamp": "2025-10-01T07:57:37.591Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": true,
      "sightingId": "24544f8a-5f51-4ef4-80a1-042243f425ae",
      "timestamp": "2025-04-11T20:26:05.851Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": false,
      "sightingId": "e3108fb9-28d6-475a-a72b-98be9b5bcfde",
      "timestamp": "2026-01-28T21:46:15.181Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": false,
      "sightingId": "95f431c9-0879-4c1e-a035-e8e7895fa998",
      "timestamp": "2025-04-08T16:58:44.045Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": true,
      "sightingId": "7df75fd5-6240-4cab-9000-d597c28b2d3c",
      "timestamp": "2025-08-07T20:57:09.338Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": false,
      "sightingId": "75dad780-6e53-4345-8439-259956001891",
      "timestamp": "2025-10-11T17:13:36.110Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": false,
      "sightingId": "ffbc2571-4925-4d78-881e-fdd9956420e5",
      "timestamp": "2025-11-08T22:56:01.889Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": true,
      "sightingId": "5a8e76e8-69bc-46b9-8ee7-5d6157142294",
      "timestamp": "2025-11-10T16:22:12.017Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": false,
      "sightingId": "bdf8b058-9eff-422e-a57b-8db47e3ea7b4",
      "timestamp": "2025-05-26T18:57:00.018Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": false,
      "sightingId": "e620cae7-e624-49a9-aded-43f83d570ed3",
      "timestamp": "2025-03-03T12:26:05.882Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": true,
      "sightingId": "829f8888-6392-4618-89de-f25b2fe91f04",
      "timestamp": "2025-02-17T18:47:15.275Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": false,
      "sightingId": "69486dbd-6143-43e4-96c9-cd614e7210f8",
      "timestamp": "2025-08-09T14:05:54.224Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": false,
      "sightingId": "49b47b20-ba91-4f0a-a892-f3b66c6dc7f9",
      "timestamp": "2026-02-09T05:55:33.424Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": true,
      "sightingId": "cdba44d6-6844-4908-90e9-47c3f848ff66",
      "timestamp": "2025-02-15T22:30:50.172Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": false,
      "sightingId": "66a22a42-891a-4b1b-9ff4-3514111c1326",
      "timestamp": "2025-07-12T04:42:08.024Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": false,
      "sightingId": "6bf1a44b-f2c3-4e1b-85cb-6ae83aff7451",
      "timestamp": "2025-06-12T01:00:28.339Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": true,
      "sightingId": "f9b934f9-221a-48cd-9844-014aef467269",
      "timestamp": "2025-08-18T20:29:22.058Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": false,
      "sightingId": "eb6f7625-5e93-4180-9798-fb5d3f849a6d",
      "timestamp": "2025-08-20T13:53:26.954Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": false,
      "sightingId": "64919f36-8a38-45d6-a954-92e3a04ca0c9",
      "timestamp": "2025-05-09T17:45:27.215Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": true,
      "sightingId": "6f6a57ec-6675-4a7e-8816-52748cd17504",
      "timestamp": "2025-07-25T03:32:11.498Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": false,
      "sightingId": "674516af-9000-4a8a-998c-acb100ca23c8",
      "timestamp": "2025-06-08T11:41:07.447Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": false,
      "sightingId": "fc969da5-053d-4299-b001-96dee4ff1d23",
      "timestamp": "2026-01-24T23:20:46.363Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": true,
      "sightingId": "77a0703a-0f0e-4b81-81b0-6fd7f710c46a",
      "timestamp": "2026-02-11T17:21:55.614Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": false,
      "sightingId": "722bff92-3373-4a7a-b167-a00ac707da0e",
      "timestamp": "2025-05-18T20:26:10.251Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": false,
      "sightingId": "73fe4d8e-5bb5-4a87-8e6a-03f2a874ba68",
      "timestamp": "2025-10-06T14:48:50.070Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": true,
      "sightingId": "d6a32404-28d8-4a67-ba56-bc4d938d1f97",
      "timestamp": "2025-04-17T12:51:15.917Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": false,
      "sightingId": "8b9289cf-cd49-4078-b9f5-648fa6e9535f",
      "timestamp": "2025-05-06T22:31:43.482Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": false,
      "sightingId": "64700830-a85e-4aff-8f0e-036d50cf7fdc",
      "timestamp": "2025-03-11T22:40:34.739Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": true,
      "sightingId": "9e49ef9c-436b-4901-ab71-5dd6d6ab5c28",
      "timestamp": "2025-07-19T15:58:03.640Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": false,
      "sightingId": "d35811c5-4a7d-43bc-a1bc-df030bf5eb94",
      "timestamp": "2025-05-26T12:06:48.614Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": false,
      "sightingId": "8e09bbd8-b4d9-428a-a072-18cb5ffa1b69",
      "timestamp": "2025-12-17T18:47:53.743Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": true,
      "sightingId": "abf39514-2a26-4de2-a0c6-66e568c8890d",
      "timestamp": "2025-02-23T00:22:03.271Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": false,
      "sightingId": "f4c53af5-6dd0-460c-a398-834fa8952c6d",
      "timestamp": "2025-05-12T09:54:07.566Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": false,
      "sightingId": "329e7856-7021-4b9a-95c6-d782ebbf2795",
      "timestamp": "2025-04-03T19:01:50.688Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": true,
      "sightingId": "c57e8dcc-7ffc-4ade-abfd-d54a086779fd",
      "timestamp": "2025-11-13T21:43:55.913Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": false,
      "sightingId": "a0823526-7eb0-4140-90ba-abbae9fea0c4",
      "timestamp": "2025-05-28T21:03:48.136Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": false,
      "sightingId": "0b92534c-d1bc-43bd-aa79-0a5a0e6b25f3",
      "timestamp": "2025-11-12T10:55:26.099Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": true,
      "sightingId": "003ba70d-0771-4863-86f3-521c31ce5d49",
      "timestamp": "2025-02-05T05:28:49.087Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": false,
      "sightingId": "1df29844-1a40-445b-91f1-e62a05f47612",
      "timestamp": "2025-05-17T08:16:31.574Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": false,
      "sightingId": "9944ff38-0533-47e3-afae-4bb619f17a45",
      "timestamp": "2026-01-10T23:44:54.070Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": true,
      "sightingId": "fc2db438-1e45-4c81-a4a4-0397df947caa",
      "timestamp": "2026-02-05T18:49:10.283Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": false,
      "sightingId": "3fbead92-0d43-4102-8ae4-7e3c0f91828e",
      "timestamp": "2025-07-28T06:03:58.206Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": false,
      "sightingId": "9bf35aa1-9f52-47d7-bb4f-8e5a6b1697fc",
      "timestamp": "2025-11-15T11:05:02.170Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": true,
      "sightingId": "8d5fc9bf-6621-4f0f-b786-8bee4bba0b10",
      "timestamp": "2025-09-01T13:49:04.061Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": false,
      "sightingId": "4796733e-3bc5-42df-bee2-f7c9adf1b093",
      "timestamp": "2025-08-01T12:29:31.170Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": false,
      "sightingId": "f1cb3526-3b9e-48d8-ab59-24e7fed9e965",
      "timestamp": "2025-12-07T21:24:06.718Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": true,
      "sightingId": "26eae8f3-7b52-4931-9231-50420f534170",
      "timestamp": "2025-07-13T12:53:42.417Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": false,
      "sightingId": "434e2df2-5596-424f-82d2-2b31869ecee2",
      "timestamp": "2025-11-25T08:48:09.193Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": false,
      "sightingId": "7c7536ec-6b70-4837-9525-1b59c30edcfc",
      "timestamp": "2025-11-01T21:38:07.544Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": true,
      "sightingId": "1bec0bb5-d208-401f-a2a3-aac5414830f3",
      "timestamp": "2025-12-13T17:48:05.206Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": false,
      "sightingId": "940d67c1-3616-4ce5-9de8-b0d0c9bfc670",
      "timestamp": "2026-01-25T01:51:26.429Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": false,
      "sightingId": "13b6530d-2103-43c0-922d-46b97bb283b2",
      "timestamp": "2025-12-06T21:11:06.206Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": true,
      "sightingId": "e725d81a-6c67-4637-a4d2-aa6096b3cfc8",
      "timestamp": "2025-03-29T16:30:31.859Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": false,
      "sightingId": "c6bce2c7-7a0b-4dcb-8e69-16bf0c7a5503",
      "timestamp": "2025-06-23T17:53:39.615Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": false,
      "sightingId": "2ebc762b-3a80-4cca-bb95-ad7b1e9ce0f6",
      "timestamp": "2025-09-29T07:17:21.955Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": true,
      "sightingId": "6cdd9632-a978-417b-967b-513c91fc7814",
      "timestamp": "2025-04-22T14:38:58.696Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": false,
      "sightingId": "588bf87d-6fb7-44f5-8e44-25e502c84de0",
      "timestamp": "2026-01-25T14:57:30.874Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": false,
      "sightingId": "0d8b4425-9704-4071-85f1-d6129b210636",
      "timestamp": "2025-09-17T11:38:10.090Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": true,
      "sightingId": "86115f22-f5aa-4f33-a58c-dd31b2403197",
      "timestamp": "2025-08-26T23:47:51.383Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": false,
      "sightingId": "fa2a5429-c551-4c80-b0d0-d0ed14176c1d",
      "timestamp": "2025-07-28T09:44:27.489Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": false,
      "sightingId": "bf9ece4c-d3f3-497b-a4b3-330f033760a1",
      "timestamp": "2025-08-31T15:19:57.926Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": true,
      "sightingId": "8d734360-d25f-42a8-9b94-5c72ab688488",
      "timestamp": "2026-01-01T07:13:07.418Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": false,
      "sightingId": "800ebde9-e16c-43e7-8215-ce7b8c22123e",
      "timestamp": "2025-09-23T13:44:12.402Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": false,
      "sightingId": "e0974c8c-dda8-4743-9797-11c8af85b678",
      "timestamp": "2025-04-05T17:18:20.639Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": true,
      "sightingId": "2a366ebd-c1a7-4672-a07b-7c8fcb90f778",
      "timestamp": "2025-04-15T18:38:12.836Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": false,
      "sightingId": "3041b4cb-0c1c-402f-acd3-fa4defe43f3d",
      "timestamp": "2025-07-20T06:00:39.425Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": false,
      "sightingId": "b1d2e08c-fcc4-44e3-ad36-fe2cefa3e39b",
      "timestamp": "2025-06-12T17:02:13.088Z"
    },
    {
      "ownerId": "8789467f-0450-404c-805f-d8bfce2980a7",
      "removeAfterMatching": true,
      "sightingId": "420b51c6-006b-4965-9769-0713db878147",
      "timestamp": "2025-04-16T15:26:21.954Z"
    },
    {
      "ownerId": "5ad6c4f7-3cf8-41fa-b928-fe255e9fd255",
      "removeAfterMatching": false,
      "sightingId": "5d8836b9-38a5-47a8-bb33-eeccccd602b7",
      "timestamp": "2025-07-13T23:34:13.294Z"
    },
    {
      "ownerId": "aa043656-a197-4d6e-b216-880c3e3fdc76",
      "removeAfterMatching": false,
      "sightingId": "d5c7b7b2-8ba2-4971-8ace-1d6116285afc",
      "timestamp": "2025-05-26T03:33:10.525Z"
    },
    {
      "ownerId": "5fd9b7b1-4687-42da-af2c-e432d4b2290d",
      "removeAfterMatching": true,
      "sightingId": "05d80471-87aa-42f0-a8c8-6f7e195fe63b",
      "timestamp": "2025-05-19T21:19:15.154Z"
    },
    {
      "ownerId": "ab895eef-033d-497f-be98-1fc7c902866a",
      "removeAfterMatching": false,
      "sightingId": "c306e2c7-6dfc-4b0c-8337-e9c4d0ee7d4d",
      "timestamp": "2025-02-03T02:35:32.083Z"
    },
    {
      "ownerId": "cda4b4f5-68e9-4425-8501-92a17db5c012",
      "removeAfterMatching": false,
      "sightingId": "2457e4c6-0768-4099-8ce1-8fe6ec255564",
      "timestamp": "2025-05-25T10:22:39.009Z"
    },
    {
      "ownerId": "dbd886b5-72d7-4aa3-8e96-afb221bb6964",
      "removeAfterMatching": true,
      "sightingId": "01808b24-e926-4c97-81d2-28b8d4949742",
      "timestamp": "2025-11-20T13:50:26.499Z"
    },
    {
      "ownerId": "1a233e0d-7913-41a9-b586-6b08f3605d2b",
      "removeAfterMatching": false,
      "sightingId": "5cb92d44-4126-43ac-a53f-e7109626ae55",
      "timestamp": "2025-05-28T20:07:06.183Z"
    },
    {
      "ownerId": "28d31d0f-2c51-425b-90f6-88676918feff",
      "removeAfterMatching": false,
      "sightingId": "0c475bb6-4bdd-482e-965e-5e4cbc5b6ae2",
      "timestamp": "2026-01-07T08:01:52.820Z"
    },
    {
      "ownerId": "6886f55c-f8f3-4ae1-b926-664a6756a523",
      "removeAfterMatching": true,
      "sightingId": "750a0ed4-f0a9-4a40-8b9f-a118e83e512a",
      "timestamp": "2026-02-12T00:59:55.878Z"
    }
  ];
