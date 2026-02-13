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
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": true,
      "sightingId": "eaf41ac5-3dd4-47c4-89ee-0a6b028f2018",
      "timestamp": "2025-03-19T15:58:34.121Z",
      "details": {
        "lat": 34.2565407709398,
        "lng": 32.368743932073116
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": false,
      "sightingId": "fb37693a-9dd4-405a-a4ee-6ac62b657c1c",
      "timestamp": "2025-12-30T20:09:46.081Z",
      "details": {
        "lat": 19.573812749407605,
        "lng": 6.394632956449237
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": false,
      "sightingId": "75353843-d908-4e66-99cb-77f1a7b62f96",
      "timestamp": "2025-08-16T19:38:04.257Z",
      "details": {
        "lat": 30.49122226850311,
        "lng": 46.899210626008525
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": true,
      "sightingId": "a6416319-9a79-437a-a5d4-1618ebc856b0",
      "timestamp": "2025-08-15T17:37:44.457Z",
      "details": {
        "lat": 12.844081428858317,
        "lng": 20.219140161148637
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": false,
      "sightingId": "558bf422-b5d8-4bd3-99c7-e865fb392004",
      "timestamp": "2025-10-26T11:00:15.962Z",
      "details": {
        "lat": 36.23720117961708,
        "lng": 1.1100881162477094
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": false,
      "sightingId": "31878d78-3d5f-4b59-a523-4cbbb6586870",
      "timestamp": "2025-07-17T09:09:25.675Z",
      "details": {
        "lat": 16.610437885385572,
        "lng": 23.021445300018563
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": true,
      "sightingId": "9709f98b-25dd-410d-97a3-9afc5d1e3c13",
      "timestamp": "2025-11-05T11:26:19.611Z",
      "details": {
        "lat": 23.64902694727531,
        "lng": 45.93013333657296
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": false,
      "sightingId": "e42e576e-f92a-4726-b36b-82bd246e9998",
      "timestamp": "2025-07-03T23:57:17.491Z",
      "details": {
        "lat": 1.0462970654556314,
        "lng": 8.377363821073535
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": false,
      "sightingId": "d8c5cff4-40dc-47f8-b827-67245f7bd6dc",
      "timestamp": "2025-09-28T19:07:50.869Z",
      "details": {
        "lat": 41.88221766891974,
        "lng": 2.4636619992208675
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": true,
      "sightingId": "3e462998-138d-4fd1-97d4-d1c54cb73cbc",
      "timestamp": "2025-07-25T12:44:31.401Z",
      "details": {
        "lat": 39.057182558622486,
        "lng": 18.71595945543534
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": false,
      "sightingId": "90ea1e4d-d8cf-42a3-90cb-295307bc88d3",
      "timestamp": "2025-12-07T00:36:02.873Z",
      "details": {
        "lat": 39.98399467859212,
        "lng": 32.123884398414425
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": false,
      "sightingId": "026527e6-bff8-4632-a628-d795da752be6",
      "timestamp": "2025-11-21T10:18:29.239Z",
      "details": {
        "lat": 29.864337844289246,
        "lng": 19.990972371280407
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": true,
      "sightingId": "cdb59ab1-eb4e-4571-9746-659573e302a7",
      "timestamp": "2025-09-07T23:46:23.992Z",
      "details": {
        "lat": 34.48379951053029,
        "lng": 46.96716325173043
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": false,
      "sightingId": "dfa9d69b-567c-437e-9e64-4708d5f7b144",
      "timestamp": "2026-01-21T17:48:18.639Z",
      "details": {
        "lat": 25.139560974479956,
        "lng": 29.754560980406364
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": false,
      "sightingId": "caae86df-692f-457b-91d8-cfb88d337560",
      "timestamp": "2025-05-10T17:58:59.672Z",
      "details": {
        "lat": 9.403675527226907,
        "lng": 40.294612178137506
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": true,
      "sightingId": "554d9b04-5d64-4eec-bed5-397590dce6d8",
      "timestamp": "2026-01-03T18:58:45.431Z",
      "details": {
        "lat": 10.43782609836828,
        "lng": 27.083332433809808
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": false,
      "sightingId": "7ad5f08d-16ba-47ce-900d-f15d7f86ba23",
      "timestamp": "2026-01-13T16:15:06.067Z",
      "details": {
        "lat": 29.333389333653713,
        "lng": 13.698657255865127
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": false,
      "sightingId": "f7405d76-b7fa-4acf-b5f3-6c928231127b",
      "timestamp": "2025-10-05T10:53:05.966Z",
      "details": {
        "lat": 37.620344025998925,
        "lng": 45.80439145567076
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": true,
      "sightingId": "652bd28d-96f0-40d6-9dd9-c34da8ad25f0",
      "timestamp": "2025-09-10T18:17:15.222Z",
      "details": {
        "lat": 31.09348461387782,
        "lng": 12.326960449445712
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": false,
      "sightingId": "6440b55b-291a-4ce4-8a8d-41daa153360a",
      "timestamp": "2025-03-16T02:34:33.757Z",
      "details": {
        "lat": 16.06188127358309,
        "lng": 7.404690633040811
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": false,
      "sightingId": "28f3067f-663f-41c3-a4ab-b0942ae35cef",
      "timestamp": "2025-03-20T22:24:59.813Z",
      "details": {
        "lat": 43.16811478741727,
        "lng": 11.888769254631715
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": true,
      "sightingId": "6cd6fff7-66b3-4ebf-abc0-26d5c6c06f62",
      "timestamp": "2025-08-22T13:45:45.453Z",
      "details": {
        "lat": 16.204905662565512,
        "lng": 4.090322802375745
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": false,
      "sightingId": "27b98a18-661d-452a-9cc1-e7465d93a146",
      "timestamp": "2025-05-27T06:59:23.691Z",
      "details": {
        "lat": 1.2870598164103408,
        "lng": 46.70038647620963
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": false,
      "sightingId": "30ff976e-f5ff-4d46-993a-fefc6e1dfcfe",
      "timestamp": "2025-12-06T17:29:35.189Z",
      "details": {
        "lat": 41.89891845855538,
        "lng": 9.640244667840225
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": true,
      "sightingId": "e28df5c0-7e0f-4d19-9769-4909b5156c61",
      "timestamp": "2025-05-03T01:13:33.131Z",
      "details": {
        "lat": 22.091951286648083,
        "lng": 21.358321001018897
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": false,
      "sightingId": "22b14a90-4f56-434a-8c4d-223e58215b1d",
      "timestamp": "2025-05-10T13:10:00.016Z",
      "details": {
        "lat": 47.27997415895187,
        "lng": 16.71597580005334
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": false,
      "sightingId": "f248424b-9505-401b-8ea6-c2c38f22ca4c",
      "timestamp": "2025-10-27T04:56:28.108Z",
      "details": {
        "lat": 26.13300919641227,
        "lng": 9.774700591370312
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": true,
      "sightingId": "a4567580-a343-43f2-af95-49626a502fd2",
      "timestamp": "2025-08-08T08:43:29.160Z",
      "details": {
        "lat": 5.829448615545147,
        "lng": 42.487327951713254
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": false,
      "sightingId": "edfb8e8c-a823-43b5-a67a-4ad1e639ac49",
      "timestamp": "2025-02-22T08:11:54.026Z",
      "details": {
        "lat": 48.54051623107865,
        "lng": 1.0283102456551285
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": false,
      "sightingId": "9ee302cc-a13d-401e-9337-3857613dff86",
      "timestamp": "2025-12-14T05:12:50.397Z",
      "details": {
        "lat": 41.02953076756907,
        "lng": 48.28036437710214
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": true,
      "sightingId": "e6407bee-6739-4998-abfb-23a13e2ac372",
      "timestamp": "2025-11-19T00:23:25.717Z",
      "details": {
        "lat": 17.25253783254822,
        "lng": 49.86814000369888
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": false,
      "sightingId": "4fb1f8e5-6951-41eb-91f0-bd6cd8c8c242",
      "timestamp": "2025-07-26T13:51:12.676Z",
      "details": {
        "lat": 35.20169077809582,
        "lng": 11.68174740648661
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": false,
      "sightingId": "c5d64acf-ed97-494d-a53e-d03b09880722",
      "timestamp": "2026-02-12T04:07:19.164Z",
      "details": {
        "lat": 34.12631198390773,
        "lng": 6.989483052618772
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": true,
      "sightingId": "7d1c513c-7840-4160-87a2-8159ea8f390a",
      "timestamp": "2025-10-16T02:52:28.196Z",
      "details": {
        "lat": 33.00114350456064,
        "lng": 27.555427363559048
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": false,
      "sightingId": "1d78d4b7-bf44-4624-966e-e09accd5edda",
      "timestamp": "2025-11-08T17:41:56.350Z",
      "details": {
        "lat": 11.605040262576278,
        "lng": 37.12628581574709
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": false,
      "sightingId": "efec29d4-ccb5-42ac-bedf-791f24739826",
      "timestamp": "2025-04-30T16:35:48.857Z",
      "details": {
        "lat": 15.33567720509007,
        "lng": 42.75225911759562
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": true,
      "sightingId": "c20ce275-5220-4d7a-bd87-f74fefb235f6",
      "timestamp": "2025-12-14T08:55:09.455Z",
      "details": {
        "lat": 3.362233762247213,
        "lng": 27.92816694400926
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": false,
      "sightingId": "1487fc14-8e08-43ef-a452-a9074910227e",
      "timestamp": "2026-01-07T11:14:08.543Z",
      "details": {
        "lat": 28.269652772739658,
        "lng": 11.128428934790568
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": false,
      "sightingId": "12a99d8e-00c7-4349-9989-aef6198d436b",
      "timestamp": "2025-10-14T00:34:25.312Z",
      "details": {
        "lat": 11.812863907307763,
        "lng": 41.484625383984564
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": true,
      "sightingId": "6f9369f5-d12a-49b2-96ae-b9fda54444f0",
      "timestamp": "2025-07-07T18:01:33.815Z",
      "details": {
        "lat": 45.816539415158125,
        "lng": 32.30687140140139
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": false,
      "sightingId": "a77ddcb9-9586-4971-9543-f37fea47c69f",
      "timestamp": "2025-08-27T23:39:17.597Z",
      "details": {
        "lat": 46.39700031567934,
        "lng": 22.71031019752947
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": false,
      "sightingId": "133d84ab-2dd9-471d-bcac-88c0a5e70e77",
      "timestamp": "2025-04-27T22:41:49.382Z",
      "details": {
        "lat": 13.612601158002736,
        "lng": 31.020884691345064
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": true,
      "sightingId": "7fb5977b-dcea-48ff-958f-324b00b00c5d",
      "timestamp": "2025-04-27T17:53:44.119Z",
      "details": {
        "lat": 34.391780736568066,
        "lng": 35.14784728332056
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": false,
      "sightingId": "8a4d13e7-13df-43fe-8994-5c6e7bf3d649",
      "timestamp": "2025-11-27T07:07:13.000Z",
      "details": {
        "lat": 17.10435299906407,
        "lng": 7.5860761495348425
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": false,
      "sightingId": "18cc05a9-b3ce-441b-b4f0-390d3007f60e",
      "timestamp": "2025-03-28T21:28:02.569Z",
      "details": {
        "lat": 6.645388038582545,
        "lng": 47.91972028944191
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": true,
      "sightingId": "eabfd082-fb50-4e82-9b0d-7ab927b045c8",
      "timestamp": "2025-10-07T19:56:19.864Z",
      "details": {
        "lat": 29.870772117041238,
        "lng": 11.61385279379521
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": false,
      "sightingId": "e83b766b-4a28-4fef-bbf4-f9f5fb764a09",
      "timestamp": "2025-09-18T22:40:41.758Z",
      "details": {
        "lat": 32.860758720918255,
        "lng": 20.34565294360017
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": false,
      "sightingId": "528dbc19-7e9f-48b9-8122-e46f347fab83",
      "timestamp": "2025-08-02T04:37:26.183Z",
      "details": {
        "lat": 7.63825148596834,
        "lng": 9.034500579573923
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": true,
      "sightingId": "cea98995-d5a6-47ac-b389-059a5ffca2f7",
      "timestamp": "2025-11-25T06:27:15.552Z",
      "details": {
        "lat": 32.28791370156114,
        "lng": 11.709832393281822
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": false,
      "sightingId": "4f755a67-b103-46e5-936c-fe0d91086e94",
      "timestamp": "2026-01-11T00:36:34.526Z",
      "details": {
        "lat": 30.786978062724945,
        "lng": 44.99887866260795
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": false,
      "sightingId": "f797b306-eab4-4d6e-a252-593b1e634d85",
      "timestamp": "2025-05-11T02:35:14.539Z",
      "details": {
        "lat": 35.27780863776619,
        "lng": 41.577608103509576
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": true,
      "sightingId": "337f13ce-723e-429f-8062-ff192d09de28",
      "timestamp": "2025-06-21T16:22:01.211Z",
      "details": {
        "lat": 32.38757913363793,
        "lng": 46.69060463985157
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": false,
      "sightingId": "e4ce5f6c-55dd-4fd4-9cbf-4f7ab84f6b9f",
      "timestamp": "2025-03-29T16:10:41.254Z",
      "details": {
        "lat": 21.41986919431294,
        "lng": 21.491973610187546
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": false,
      "sightingId": "a936756f-d908-4846-a187-7ca595328395",
      "timestamp": "2025-07-30T06:11:40.981Z",
      "details": {
        "lat": 35.95464342250736,
        "lng": 34.86607922039877
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": true,
      "sightingId": "3113d321-20e8-4c95-b734-581da57c1481",
      "timestamp": "2026-01-25T17:27:18.764Z",
      "details": {
        "lat": 22.813139577092272,
        "lng": 47.784629200915376
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": false,
      "sightingId": "412d5058-b665-43cb-a00e-1352f5f8bef3",
      "timestamp": "2025-06-11T17:18:05.726Z",
      "details": {
        "lat": 43.580850729420234,
        "lng": 24.1145138387954
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": false,
      "sightingId": "1b1cecb9-9ed0-4974-a8cc-b64bb2d0d34f",
      "timestamp": "2025-12-28T08:19:42.782Z",
      "details": {
        "lat": 1.3713873295434442,
        "lng": 46.705158234445626
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": true,
      "sightingId": "86cec4a9-f44f-4ec6-a54e-3fc25c59c834",
      "timestamp": "2025-10-25T19:44:37.047Z",
      "details": {
        "lat": 11.988112481815788,
        "lng": 31.728643020196223
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": false,
      "sightingId": "7c98ad84-4744-487b-8f95-67ea8c184554",
      "timestamp": "2025-02-17T23:30:51.865Z",
      "details": {
        "lat": 4.598249342724891,
        "lng": 0.7358720471303537
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": false,
      "sightingId": "8b7faa50-d7d6-4139-bf32-e25e35e131d3",
      "timestamp": "2025-12-03T04:53:26.636Z",
      "details": {
        "lat": 27.705501004894195,
        "lng": 0.02349472471468439
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": true,
      "sightingId": "0c68fee0-65f2-46f9-be6d-5a2d255f616b",
      "timestamp": "2025-08-27T21:54:47.240Z",
      "details": {
        "lat": 1.7558543927553016,
        "lng": 20.933478679966555
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": false,
      "sightingId": "c4ab4e93-29f7-4590-a996-eb37a9a38b22",
      "timestamp": "2025-09-09T06:16:08.045Z",
      "details": {
        "lat": 30.435630386772296,
        "lng": 30.467794144516525
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": false,
      "sightingId": "0bba86a2-9a2c-4aa9-8399-9b51a7857bd8",
      "timestamp": "2025-04-25T19:35:08.893Z",
      "details": {
        "lat": 4.786416972744412,
        "lng": 1.6221949900317612
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": true,
      "sightingId": "5b1faef2-5744-4792-8abd-798922309af4",
      "timestamp": "2025-09-17T13:53:50.928Z",
      "details": {
        "lat": 12.862850356558353,
        "lng": 27.052769909690582
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": false,
      "sightingId": "056949a0-b024-4b36-b2bc-8a5851075c1a",
      "timestamp": "2025-10-13T00:33:43.689Z",
      "details": {
        "lat": 19.126828181002566,
        "lng": 6.846175312418029
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": false,
      "sightingId": "f9a47737-1292-44f3-9f7d-0aa25676563d",
      "timestamp": "2026-01-24T03:17:13.261Z",
      "details": {
        "lat": 14.259348104242232,
        "lng": 15.039772022137821
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": true,
      "sightingId": "65b88219-d93e-4afd-bc59-f9085e2b97b9",
      "timestamp": "2025-12-22T16:23:34.519Z",
      "details": {
        "lat": 4.148933524223219,
        "lng": 6.284745766319793
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": false,
      "sightingId": "60df1d4a-5123-4033-be3b-149e9b6cebcc",
      "timestamp": "2025-09-16T06:53:19.012Z",
      "details": {
        "lat": 5.0364412390121345,
        "lng": 41.48294132346758
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": false,
      "sightingId": "de9852e1-ce77-470b-afc5-07087be19d26",
      "timestamp": "2025-10-07T05:35:14.006Z",
      "details": {
        "lat": 30.253211988294392,
        "lng": 27.06847544526287
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": true,
      "sightingId": "0674a553-6147-4670-9b0e-57983bc2d772",
      "timestamp": "2025-12-16T22:10:25.815Z",
      "details": {
        "lat": 3.40721448718368,
        "lng": 42.1987197693053
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": false,
      "sightingId": "ffe4305a-903e-4439-b0d3-5eb0319f6cf5",
      "timestamp": "2026-01-11T02:44:17.978Z",
      "details": {
        "lat": 7.829605573324044,
        "lng": 39.01579545282347
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": false,
      "sightingId": "f0466436-7f3e-4efe-bc9f-88a15237701c",
      "timestamp": "2025-07-20T23:33:24.006Z",
      "details": {
        "lat": 5.595585352456261,
        "lng": 49.689873007315796
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": true,
      "sightingId": "0df37278-e927-43c8-9653-195bd642cfd3",
      "timestamp": "2026-01-09T14:47:38.776Z",
      "details": {
        "lat": 10.656775640349903,
        "lng": 2.450270732115073
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": false,
      "sightingId": "83538d6b-03f6-4481-b98e-404ff30db36a",
      "timestamp": "2025-07-17T06:35:51.207Z",
      "details": {
        "lat": 37.88806061800424,
        "lng": 3.2523915294835835
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": false,
      "sightingId": "e8c0481c-93d0-4443-b07e-027bbc9c8c14",
      "timestamp": "2025-07-09T08:05:22.965Z",
      "details": {
        "lat": 23.911702897691423,
        "lng": 21.05397815410158
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": true,
      "sightingId": "3193d692-b001-4f45-8f41-34b672dafcda",
      "timestamp": "2026-01-17T23:59:00.146Z",
      "details": {
        "lat": 1.9540102261153325,
        "lng": 12.308765223617046
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": false,
      "sightingId": "79e2f44d-da61-42f1-98c7-78bc467e56f0",
      "timestamp": "2026-02-06T19:29:51.233Z",
      "details": {
        "lat": 10.916629168880215,
        "lng": 38.93204566200618
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": false,
      "sightingId": "764d3a21-abbb-47bc-8993-e6f56216b9f9",
      "timestamp": "2025-03-13T01:00:58.911Z",
      "details": {
        "lat": 23.530607606654367,
        "lng": 35.7054387748747
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": true,
      "sightingId": "6d5ab881-e1fa-4be0-a0e0-e619ad26dda4",
      "timestamp": "2025-02-13T01:25:07.377Z",
      "details": {
        "lat": 0.8198103684587843,
        "lng": 30.447574705880665
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": false,
      "sightingId": "ded8e8fa-3251-4691-bb13-30d2d4446db5",
      "timestamp": "2026-01-13T02:30:24.922Z",
      "details": {
        "lat": 31.15149332770327,
        "lng": 45.56113063475524
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": false,
      "sightingId": "27fc05c1-e9ba-4395-a1f2-b0a0809660ab",
      "timestamp": "2025-11-05T19:41:13.527Z",
      "details": {
        "lat": 14.648631408323636,
        "lng": 49.78009126303118
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": true,
      "sightingId": "49963ec8-cd16-426e-b021-111f17c13158",
      "timestamp": "2025-07-17T19:57:03.484Z",
      "details": {
        "lat": 18.58031276590068,
        "lng": 34.10633655266393
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": false,
      "sightingId": "544e1836-fb6f-429a-82bc-cc7a820b6728",
      "timestamp": "2025-06-02T18:06:39.082Z",
      "details": {
        "lat": 30.099640347166694,
        "lng": 13.909065717706737
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": false,
      "sightingId": "2a642722-c6c3-48e7-bf19-ed296f3991f1",
      "timestamp": "2025-06-09T01:59:21.889Z",
      "details": {
        "lat": 28.720668199507138,
        "lng": 9.922009554008893
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": true,
      "sightingId": "1ecb8615-8cd9-4696-8599-7d3d0484a521",
      "timestamp": "2025-07-17T14:55:22.574Z",
      "details": {
        "lat": 30.787584899616192,
        "lng": 13.830340520501554
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": false,
      "sightingId": "88c9b6f5-e3af-4866-bedc-cea35ebe1326",
      "timestamp": "2025-10-31T09:59:52.617Z",
      "details": {
        "lat": 46.52626839716151,
        "lng": 44.13417835721784
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": false,
      "sightingId": "f69e2cb8-1850-48fd-940d-0d58bcc672e4",
      "timestamp": "2025-09-12T09:40:11.342Z",
      "details": {
        "lat": 47.584603772290336,
        "lng": 0.33011235341399714
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": true,
      "sightingId": "f691f115-2941-4ea6-a99c-616591ca25f4",
      "timestamp": "2025-11-14T07:52:48.354Z",
      "details": {
        "lat": 44.622113652865245,
        "lng": 37.29848624766913
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": false,
      "sightingId": "0b0c45d7-5fee-46cb-aa3f-cb074fe02fe6",
      "timestamp": "2025-02-05T21:06:46.762Z",
      "details": {
        "lat": 42.921521849295125,
        "lng": 16.541716554465573
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": false,
      "sightingId": "220b3c53-ef29-41cf-85cf-f942f8f207b8",
      "timestamp": "2025-03-10T01:01:56.963Z",
      "details": {
        "lat": 49.91207893752525,
        "lng": 4.301278703463707
      }
    },
    {
      "ownerId": "7c2c6c69-2744-472e-9b0f-db41428409b3",
      "removeAfterMatching": true,
      "sightingId": "1897e0b1-80b5-454d-a251-90c41185ca03",
      "timestamp": "2025-06-13T03:19:16.299Z",
      "details": {
        "lat": 35.362187434461745,
        "lng": 3.59261462056113
      }
    },
    {
      "ownerId": "b9ef58ed-55f3-43dc-be57-74c52ab6d2cf",
      "removeAfterMatching": false,
      "sightingId": "095d3e7d-22d9-4e67-b4fe-9a41c7ad48b9",
      "timestamp": "2025-07-01T09:19:58.658Z",
      "details": {
        "lat": 13.288861225838527,
        "lng": 16.117880016457054
      }
    },
    {
      "ownerId": "0e314856-3622-4b8e-a719-2643bede18d9",
      "removeAfterMatching": false,
      "sightingId": "1c5158ec-428f-4d62-b5ff-47039dd74a7a",
      "timestamp": "2025-12-28T22:54:47.760Z",
      "details": {
        "lat": 48.968931223626825,
        "lng": 36.37520447458392
      }
    },
    {
      "ownerId": "a06857c9-5ea2-4845-b5ce-493923f6adbd",
      "removeAfterMatching": true,
      "sightingId": "b6305fb8-617f-4539-9a4a-12eb701bdcee",
      "timestamp": "2026-01-28T16:05:04.837Z",
      "details": {
        "lat": 9.350102791501131,
        "lng": 6.065823777760765
      }
    },
    {
      "ownerId": "3edd043a-36c8-4632-8e4a-32672ee4ea3c",
      "removeAfterMatching": false,
      "sightingId": "b55714ce-5de7-49a7-a249-cb4253af597b",
      "timestamp": "2025-04-11T10:36:36.962Z",
      "details": {
        "lat": 40.59526772992136,
        "lng": 39.64074678264788
      }
    },
    {
      "ownerId": "191492a9-bf69-44f7-bb2f-7a62fe9cbca1",
      "removeAfterMatching": false,
      "sightingId": "37250e2f-1579-4340-b84b-8ec95298a6a8",
      "timestamp": "2025-02-11T12:21:01.003Z",
      "details": {
        "lat": 34.606346428703155,
        "lng": 37.06444157883123
      }
    },
    {
      "ownerId": "b31ca0b4-dea4-45cc-ad04-4eec004a2134",
      "removeAfterMatching": true,
      "sightingId": "ce6316b1-66ec-402d-87dd-d1800160b539",
      "timestamp": "2026-01-31T14:10:17.787Z",
      "details": {
        "lat": 13.304072724388472,
        "lng": 26.55733961835125
      }
    },
    {
      "ownerId": "03e02209-2c59-4e49-a6fc-7e6566128e88",
      "removeAfterMatching": false,
      "sightingId": "0cece2d9-c690-42c8-a4b9-60afafdcbb20",
      "timestamp": "2026-02-09T20:44:16.305Z",
      "details": {
        "lat": 40.62141576669099,
        "lng": 28.88421263807686
      }
    },
    {
      "ownerId": "61dccb57-dd2e-44ec-9ab5-4b12436aa37d",
      "removeAfterMatching": false,
      "sightingId": "38decdb1-6473-4b6e-b55d-0278506ae98d",
      "timestamp": "2026-01-12T14:46:22.598Z",
      "details": {
        "lat": 18.459794607435498,
        "lng": 17.104439356166022
      }
    },
    {
      "ownerId": "2c6baf7b-1a42-49dd-b789-da0e029cb68f",
      "removeAfterMatching": true,
      "sightingId": "b46f266a-db97-425a-8738-61dc9d2953c4",
      "timestamp": "2025-06-16T18:29:12.324Z",
      "details": {
        "lat": 26.05580391058459,
        "lng": 3.2490612785973205
      }
    }
  ];
