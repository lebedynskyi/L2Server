## ADDED Requirements

### Requirement: Attack hits are scheduled, not polled
The system SHALL resolve attack hits via an explicitly scheduled delayed task rather than a fixed-period poll loop, so the delay before the first hit and the delay between successive hits are not quantized to a tick period.

#### Scenario: First hit fires at the computed delay, not on the next poll boundary
- **WHEN** a creature starts attacking a target with a computed attack delay of `D` ms
- **THEN** the hit resolves at approximately `D` ms after the attack started, independent of any fixed tick period

#### Scenario: Successive hits are individually scheduled
- **WHEN** a hit resolves and the creature continues attacking the same target
- **THEN** the next hit is scheduled at the next computed delay from that hit's resolution time, not from the boundary of any shared poll cycle

### Requirement: Attack scheduling is cancellable via a single handle
The system SHALL expose one cancellation handle per attacking creature that, when cancelled, stops any pending or future-rescheduled attack hit for that creature.

#### Scenario: Changing intention cancels the pending attack
- **WHEN** a creature's current intention is `ATTACK` with a pending scheduled hit, and a manager swaps that creature's intention away from `ATTACK`
- **THEN** that manager cancels the pending hit task (via the attack-cancellation hook) before the new intention takes effect

#### Scenario: Target no longer valid stops the reschedule chain
- **WHEN** a scheduled hit executes and the creature is no longer attacking the same target (target dead, out of range, or intention changed away from `ATTACK`)
- **THEN** the task does not reschedule itself and no further hits occur for that attack session

### Requirement: Scheduled task execution honors its delay and is cancellable
The system SHALL provide a task scheduling mechanism where a requested delay is actually waited out before execution, and the caller receives a handle capable of cancelling the scheduled execution before it runs.

#### Scenario: Scheduling with a nonzero delay defers execution
- **WHEN** a task is scheduled with delay `D > 0`
- **THEN** the task's execution does not begin until at least `D` ms have elapsed

#### Scenario: Caller can cancel before execution
- **WHEN** a task is scheduled and the returned handle is cancelled before the delay elapses
- **THEN** the task never executes

### Requirement: Concurrent combat state mutation is serialized
The system SHALL ensure that hit resolution for different creatures does not mutate shared combat-relevant creature state (intention, HP, attack timing data) concurrently from multiple threads.

#### Scenario: Two creatures attacking simultaneously do not race
- **WHEN** two different creatures each have a hit scheduled to resolve at overlapping times
- **THEN** their hit-resolution logic does not execute concurrently on different threads
