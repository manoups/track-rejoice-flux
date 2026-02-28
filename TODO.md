- [ ] Rename prefix on WeightedAssociation Id
- [ ] Add a distance and score calculation to the WeightedAssociation
- [ ] Check facet updates via tracker
- [ ] Check metrics
- [ ] Add tags to sightings
- [ ] Add tags to content
- [ ] Decouple content from sightings
- [ ] Basic service vs extra services checkout
- [ ] Make shopping basket
- [x] Immutable objects should support multipoints
- [x] When a Sighting is deleted, it should remove from all proposed sighings
- [ ] Validate payment success upon event reception
- [ ] Add a PaymentGateway role
- [x] Memoize reference-data

### Measure metrics
- Active sessions gauge: size of openSessions and total sessions.
- Per‑user session count: distribution (min/avg/max) or a histogram. 
- Outbound update count: increment when you send a UiUpdate. 
- Outbound update bytes (approx): size of serialized patch.
- Serialization/patch time: timer around diff+serialization.
- Send failures: count exceptions in the inner send try/catch.
- Dropped/closed sessions: count when !session.isOpen() pruning happens.

#### Alive‑check / socket lifecycle
- Socket opens/closes: count in UiUpdateSocketEndpoint.startListening/stopListening.
- Close reasons: if you can access close codes, bucket them (1000, 1001, 1002, 1006). If not available, track “timeout close” via a separate counter in your ping timeout path (if you expose it).

## Screens
### Home
- [ ] Home screen
### Content
#### Post
- [ ] Create Content screen
- [ ] Update Content screen
#### Get
- [ ] My Content listing
### Sightings
#### Post
- [ ] Create Sighting
#### Get
- [ ] Sightings list
