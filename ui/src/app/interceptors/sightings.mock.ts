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
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "a9995a38-a85c-4c1e-9ec7-388b07732cc1"
      },
      "timestamp": "2025-10-21T19:14:34.078Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "e3fe0c95-06ad-4bde-a507-1b62ebcaa2b3"
      },
      "timestamp": "2025-03-04T17:23:56.147Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "cecdea97-9983-4b49-93f3-34b2ffa72998"
      },
      "timestamp": "2025-10-04T17:34:47.385Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "5525813f-604e-4d0e-900f-d2047b7c8e9c"
      },
      "timestamp": "2025-10-03T23:36:23.724Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "4f2419b2-4bca-48ed-bc28-de9826cb2c7e"
      },
      "timestamp": "2026-02-11T08:57:02.032Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "6f9b24f5-e010-45d8-89ff-2fbf0ee1a8c0"
      },
      "timestamp": "2025-07-12T13:40:35.774Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "2d6dfc46-bdb6-420c-9dc1-bb71e984278b"
      },
      "timestamp": "2025-08-07T11:24:56.700Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "9bf4abf0-5b7f-4fda-8fb5-18c1446a889a"
      },
      "timestamp": "2025-02-16T11:25:54.531Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "5a5922b3-7195-462e-be48-223f0e5edb9d"
      },
      "timestamp": "2025-10-26T19:46:46.852Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "bbca409a-108e-4c8d-a490-c3cc5d164f24"
      },
      "timestamp": "2026-02-11T13:47:45.266Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "7e9b9d8c-2fde-47ad-b5fd-aaf5fc54eaee"
      },
      "timestamp": "2026-01-22T19:16:20.044Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "1080c223-46f5-401d-8832-30210353809b"
      },
      "timestamp": "2025-10-09T05:33:18.483Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "9cc27162-0eee-4e38-b4e5-1910dd001fdf"
      },
      "timestamp": "2025-03-28T00:32:49.837Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "3d7f7ed8-ccd7-48b3-ac02-bdf4db97e2fb"
      },
      "timestamp": "2025-05-31T16:54:50.454Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "d1b82841-887a-42a8-87fe-7816a9eaeb62"
      },
      "timestamp": "2025-05-29T04:43:32.697Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "9c7ec89b-db65-4b23-96bf-5f894c7753a3"
      },
      "timestamp": "2025-05-16T23:39:49.614Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "18b13be6-2cdf-4a49-a939-92b445838303"
      },
      "timestamp": "2025-02-10T04:36:33.204Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "f6c95168-5f6a-47a1-95d6-25c782cb9da7"
      },
      "timestamp": "2025-11-08T03:15:38.145Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "fed5990a-9c50-466b-ac72-1b59bfcba868"
      },
      "timestamp": "2025-08-27T19:34:46.778Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "03390edd-8f80-4ae7-9758-11dd545bfef3"
      },
      "timestamp": "2025-08-17T00:02:05.063Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "aabb6b9a-8da7-402e-bfb2-6d807019c7c2"
      },
      "timestamp": "2025-03-29T08:53:12.734Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "ddcee3ad-6d65-4e87-af4c-4f89579479ee"
      },
      "timestamp": "2025-07-15T18:24:05.993Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "00cc4e8c-ffed-42a0-9b85-ca9fcb49a8d0"
      },
      "timestamp": "2025-08-18T12:01:01.864Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "6090a99a-6653-4695-a143-a8921f6e37c0"
      },
      "timestamp": "2026-01-09T09:41:34.519Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "a49febcb-c9ac-4b74-a8da-076f135acf1c"
      },
      "timestamp": "2025-04-29T00:04:11.201Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "eacf7881-232a-4fe7-8225-4e49c9624380"
      },
      "timestamp": "2026-02-11T01:31:22.642Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "ae1ab611-df57-4607-bc68-108104e5fb32"
      },
      "timestamp": "2025-08-05T18:18:34.022Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "b4dbceca-1fd3-4874-bfba-b7445a2c75c4"
      },
      "timestamp": "2025-04-22T10:00:20.715Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "96be141c-1705-49be-aea1-9c62cb11b114"
      },
      "timestamp": "2025-08-28T10:55:50.503Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "efbc1ec5-ed80-4486-8e18-c1768f5ec7d7"
      },
      "timestamp": "2025-04-17T16:01:24.754Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "fb5b80cc-d9ab-4c93-97e6-e35ddecbe636"
      },
      "timestamp": "2025-12-08T01:33:26.393Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "e324c1e0-54c3-4e35-960e-8a10be1a7d46"
      },
      "timestamp": "2025-05-22T20:52:52.600Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "a0b7b58f-2949-4f3c-bb31-6399ba95f239"
      },
      "timestamp": "2025-08-22T15:10:29.146Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "9c7878ef-0556-4066-9b08-d553adf8ada1"
      },
      "timestamp": "2025-06-13T00:07:48.033Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "425deffa-0007-4e6e-af24-9629442c23f5"
      },
      "timestamp": "2025-03-22T12:52:56.172Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "eda84c4e-415f-49df-9a4c-b2708afcc98e"
      },
      "timestamp": "2025-07-14T07:27:22.533Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "ef51fde7-8f04-4a1d-b281-2f891012c0e5"
      },
      "timestamp": "2025-03-16T20:01:48.887Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "b20d7074-97c8-435e-b8cd-3795bf75404b"
      },
      "timestamp": "2025-03-25T14:55:57.769Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "7e0869f3-f151-40be-8441-606e21f7d7e7"
      },
      "timestamp": "2025-05-25T02:55:22.928Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "82abd97d-3b6d-4a7c-84ed-90f705ed6d3e"
      },
      "timestamp": "2025-08-18T10:45:07.837Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "c02a28dc-836a-4ceb-b0f7-619af9b0f648"
      },
      "timestamp": "2025-05-30T12:51:50.625Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "8af69a97-f124-4dd9-aaca-ca2bc3440d46"
      },
      "timestamp": "2025-06-19T14:14:59.689Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "bb7b1312-bb35-45c4-87b0-38f403fca885"
      },
      "timestamp": "2025-09-27T08:04:05.771Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "e6fcab7e-f86d-436a-8134-7ec9ff7e8f1b"
      },
      "timestamp": "2025-09-21T09:55:08.895Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "c8e87c87-63bc-4ac3-850a-aba5f107b8dd"
      },
      "timestamp": "2025-11-26T10:36:28.033Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "26d3c2a3-2daf-41a8-aa2c-483dfaa7b95f"
      },
      "timestamp": "2026-02-01T18:55:30.788Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "cac9fe5b-3d53-4391-a891-7259aa2b581d"
      },
      "timestamp": "2025-04-11T10:00:13.170Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "02747645-7208-454d-a771-ee137acd22c7"
      },
      "timestamp": "2025-04-26T18:53:02.599Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "fc92ce12-0209-49c6-9668-ed96390fadb0"
      },
      "timestamp": "2025-12-05T00:54:05.406Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "b3309fb0-ea9a-4ed7-8312-6925f9ac9f7b"
      },
      "timestamp": "2025-11-06T05:41:45.536Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "17ef7b5d-da21-4b0e-ab5f-fa518e441d68"
      },
      "timestamp": "2025-04-23T19:23:59.135Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "f4d6e8be-b6d2-43e2-8c7f-921896b70d51"
      },
      "timestamp": "2025-09-03T07:05:23.520Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "ae160f0a-903c-4229-90b8-0df96b8de331"
      },
      "timestamp": "2025-12-16T14:02:23.938Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "aa21a347-ddb3-49be-ab1d-47f5c65428fe"
      },
      "timestamp": "2025-11-12T03:37:09.678Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "9528b36e-6b97-4fba-aa77-db49cdac7684"
      },
      "timestamp": "2025-06-12T04:10:57.498Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "90d6f35e-9b2d-4389-a703-135f4e27925b"
      },
      "timestamp": "2025-07-31T06:36:46.621Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "bdb51811-e3f7-46a3-aad7-c7ca7b990560"
      },
      "timestamp": "2025-04-11T09:36:49.133Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "2e462cbb-89b4-43f6-a0ce-88f255c59eb7"
      },
      "timestamp": "2025-07-24T05:10:29.132Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "ee4ef368-b256-4f9a-8f0f-edad399812fa"
      },
      "timestamp": "2025-05-18T05:47:06.302Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "94e4c484-2400-48f3-a472-4792ba8e665a"
      },
      "timestamp": "2025-03-11T00:51:09.897Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "12f2c29d-1a2e-427b-8d25-2bec5f040e06"
      },
      "timestamp": "2026-02-02T22:43:45.663Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "0893fd23-f75e-4d1a-8d3c-1a9f6d6b9606"
      },
      "timestamp": "2025-03-19T01:02:42.789Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "1ef84860-8fb0-418f-9b15-fbc6d84edac7"
      },
      "timestamp": "2025-06-10T21:14:14.008Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "1414d72e-2559-4a82-abab-7f9d408dcb5b"
      },
      "timestamp": "2025-04-04T07:59:29.487Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "6ec6275e-07cf-43ed-8cf1-cbdbb57ec2cf"
      },
      "timestamp": "2025-07-26T18:00:42.274Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "34dee828-d967-481c-86a1-9310569bf356"
      },
      "timestamp": "2025-08-05T13:25:18.811Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "dec78a61-073c-4220-9f6e-52aa1feba459"
      },
      "timestamp": "2025-03-28T02:18:52.702Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "d4a72639-18de-47b9-8679-1681160176ec"
      },
      "timestamp": "2025-11-10T06:15:08.257Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "e2e956b5-d6cf-4b74-a1a2-d1c0cb6f6cfe"
      },
      "timestamp": "2025-07-28T12:13:42.647Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "de9340fe-ab92-4910-bf8a-8ff27324f7ed"
      },
      "timestamp": "2025-11-24T07:33:39.825Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "4cd27155-be9f-4db6-a628-6fb7d77b72da"
      },
      "timestamp": "2025-03-18T02:03:46.173Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "a5bc49c3-c64b-4d7f-aa8d-80a948998679"
      },
      "timestamp": "2025-09-29T02:49:18.487Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "138e59fa-4966-45b6-8e2e-2e258dcfbff4"
      },
      "timestamp": "2025-11-20T08:47:24.790Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "e6f89373-618d-41ae-82c9-589dcaa0b314"
      },
      "timestamp": "2025-11-17T12:17:47.779Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "2756fdda-eaac-42ea-961e-0a6a2013b47d"
      },
      "timestamp": "2025-06-26T04:14:21.883Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "fadd1562-4a2e-42ce-bef8-79bf7bba13fc"
      },
      "timestamp": "2025-12-26T01:38:08.625Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "14f49104-7723-485d-a554-f693c652d948"
      },
      "timestamp": "2025-09-17T06:21:24.170Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "798ee405-766f-4c75-b1a6-90a677ae0f48"
      },
      "timestamp": "2025-12-17T13:10:00.377Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "bba6dce3-b12d-4b56-9ae2-134eb1ecd7a8"
      },
      "timestamp": "2025-06-23T18:18:24.663Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "73cecde4-1ecc-48a4-a9b2-2d2fd28464b2"
      },
      "timestamp": "2025-12-24T20:56:22.571Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "bac511cb-7935-44e9-a35d-eb850104b525"
      },
      "timestamp": "2025-07-07T20:11:01.994Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "be43180f-9388-4f8f-9e33-9f09cd0726d2"
      },
      "timestamp": "2025-11-26T20:07:07.077Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "79bb8024-cb4d-43d6-aed2-9461838f0459"
      },
      "timestamp": "2026-01-18T03:41:30.473Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "83b0ae63-e704-4625-bde3-f166d34f06ef"
      },
      "timestamp": "2025-08-01T06:23:16.922Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "4c946af2-1a58-449d-b0ae-e188ff3581ac"
      },
      "timestamp": "2025-07-20T09:34:38.767Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "61a1504b-419b-485b-86f3-0e831400654e"
      },
      "timestamp": "2025-12-18T22:23:32.036Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "d929bd4b-d3fc-4977-8210-114144df44e8"
      },
      "timestamp": "2025-06-17T21:06:52.772Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "3daa9c1c-d260-4176-b9b7-3615ab25549e"
      },
      "timestamp": "2025-06-26T21:26:59.840Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "f7a924c2-f4e2-4d22-9efd-7d48413e32fe"
      },
      "timestamp": "2025-12-20T14:14:21.491Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "e2edacd5-d774-42c6-af98-e5eb6b7bca16"
      },
      "timestamp": "2025-09-03T07:13:02.068Z"
    },
    {
      "ownerId": {
        "type": "20ce3ace-82cf-413b-9568-51cc014ad753"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "2b25978c-230f-4e4a-b923-65f7870660da"
      },
      "timestamp": "2025-06-09T01:15:12.335Z"
    },
    {
      "ownerId": {
        "type": "8526da30-5964-42da-ac64-9965c7df04d6"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "7ea7ad15-e5c5-4c52-a152-e37339b8a570"
      },
      "timestamp": "2025-10-28T20:49:53.409Z"
    },
    {
      "ownerId": {
        "type": "4c1a0d2a-8061-4a7e-b695-90b8b7808b19"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "e6b15bdf-5baf-4c97-9e95-9b3fcbd14e00"
      },
      "timestamp": "2025-02-26T23:22:51.272Z"
    },
    {
      "ownerId": {
        "type": "ba22d920-f201-4f64-ad20-6c3f81cd0ccf"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "b8bc086d-fe25-4061-aba5-7c00a9f625a1"
      },
      "timestamp": "2025-04-01T05:06:49.218Z"
    },
    {
      "ownerId": {
        "type": "93ca3a76-6c3b-45cd-8f6f-a51358bf86ff"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "72d09d65-9ce7-4aea-bba7-ea9c0faeda34"
      },
      "timestamp": "2025-06-27T21:11:05.305Z"
    },
    {
      "ownerId": {
        "type": "ca2f3edb-a6bb-4bcc-88f6-015b1d4d7350"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "528983a3-4f49-479c-b717-e490d758877b"
      },
      "timestamp": "2025-03-07T16:26:10.460Z"
    },
    {
      "ownerId": {
        "type": "26c8526d-3256-46be-afcf-733d544960a9"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "b63f0774-3533-47a6-ba85-6d2fdb9c6dc0"
      },
      "timestamp": "2026-01-27T03:04:44.928Z"
    },
    {
      "ownerId": {
        "type": "c61e7d9a-f793-48e7-bd09-d9fbf1c4c064"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "619e1a81-72b3-42ca-87d1-b07d1211e8a8"
      },
      "timestamp": "2025-06-17T09:28:00.652Z"
    },
    {
      "ownerId": {
        "type": "46e81b01-9de0-447a-b3ef-bb6d5d2156b5"
      },
      "removeAfterMatching": false,
      "sightingId": {
        "type": "8101d364-562c-403e-9d64-7f457cba2d42"
      },
      "timestamp": "2025-05-20T23:13:04.705Z"
    },
    {
      "ownerId": {
        "type": "209b39de-8e96-4076-8958-7b92974299c7"
      },
      "removeAfterMatching": true,
      "sightingId": {
        "type": "0ca39f9d-0cd8-42bf-b437-32d26686c437"
      },
      "timestamp": "2025-07-30T06:19:54.212Z"
    }
  ];
