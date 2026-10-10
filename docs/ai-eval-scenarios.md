# AI Eval Scenarios

This document contains manual evaluation scenarios for the reservation AI flow.
Use these scenarios after prompt, tool, recommendation, or date/time changes.

## How to Run Manual Eval

1. Start the application.
2. Login and copy the JWT token.
3. Send each scenario message to the AI chat endpoint with the same `conversationId` inside one scenario.
4. Check the AI response.
5. Check `ai_tool_call_log` for tool input/output.
6. Mark the scenario as passed only if both the user-facing response and tool calls match the expected result.

## Common Request Template

```json
{
  "conversationId": "eval-scenario-1",
  "message": "USER_MESSAGE_HERE"
}
```

Use the JWT token in the `Authorization` header:

```text
Authorization: Bearer TOKEN_HERE
```

## Scenario 1: Default Room Recommendation Uses Closest Match

### User Message

```text
Rezervisi sobu za sutra od 10 do 12 za 8 ljudi.
```

### Expected Result

- AI should call `findAvailableRooms`.
- Tool input date should be tomorrow relative to the application current date.
- Tool input should not contain a past date.
- Tool input `capacityPreference` should be `CLOSEST_MATCH`.
- AI should recommend the closest suitable room, not the most spacious room.
- AI should not mention `capacityPreference` unless asked for debugging details.

### What to Check in `ai_tool_call_log`

- `tool_name = findAvailableRooms`
- `input` contains the expected explicit date.
- `input` contains `"capacityPreference":"CLOSEST_MATCH"`.
- `success = true` if rooms are available.

## Scenario 2: Explicit Spacious Preference Uses Most Spacious

### User Message

```text
Treba mi komfornija sala za sutra od 10 do 12 za 8 ljudi.
```

### Expected Result

- AI should call `findAvailableRooms`.
- Tool input date should be tomorrow relative to the application current date.
- Tool input `capacityPreference` should be `MOST_SPACIOUS`.
- AI should recommend the most spacious available suitable room.
- AI should use the recommendation reason returned by the tool.
- AI should not invent a recommendation reason.

### What to Check in `ai_tool_call_log`

- `tool_name = findAvailableRooms`
- `input` contains `"capacityPreference":"MOST_SPACIOUS"`.
- `output` contains a recommendation.
- AI response reason matches the tool recommendation reason.

## Scenario 3: Missing Required Information

### User Message

```text
Rezervisi mi sobu za sutra.
```

### Expected Result

- AI should not call `findAvailableRooms`.
- AI should ask a concise follow-up question for missing information.
- Missing information should include time range and capacity.

### What to Check in `ai_tool_call_log`

- There should be no `findAvailableRooms` log for this message.

## Scenario 4: Past Date Should Not Be Used Accidentally

### User Message

```text
Rezervisi sobu za sutra od 10 do 12 za 10 ljudi.
```

### Expected Result

- AI should resolve `sutra` using the internal current date/time from the prompt.
- AI should not use old dates such as `2023-10-06` or `2023-10-25`.
- If the resolved time is already in the past for the current day, AI should ask for a valid future time instead of forcing a tool call.

### What to Check in `ai_tool_call_log`

- `input` date is correct for tomorrow.
- `error_message` should not be `Start time must be in the future or present` unless the user actually requested a past time.

## Scenario 5: Prepare Pending Meeting Reservation

### Flow

Send the first message:

```text
Rezervisi sobu za sutra od 10 do 12 za 8 ljudi. Naziv sastanka je PI planiranje, opis je kvartalno planiranje.
```

After AI lists available rooms, choose one:

```text
Izaberi Room 1.
```

### Expected Result

- AI should first call `findAvailableRooms`.
- AI should then call `prepareMeetingReservation` after the user chooses a room.
- Pending reservation should contain:
  - selected room id
  - local ISO `startTime`, for example `2026-10-07T10:00:00`
  - local ISO `endTime`, for example `2026-10-07T12:00:00`
  - `meetingName = PI planiranje`
  - `meetingDescription = kvartalno planiranje`
- Pending reservation tool input should not include `userId`.
- Pending reservation `startTime` and `endTime` should not include timezone offset such as `+02:00`.

### What to Check in `ai_tool_call_log`

- `tool_name = findAvailableRooms`
- `tool_name = prepareMeetingReservation`
- `prepareMeetingReservation` input contains local date-time strings without timezone offset.

## Scenario 6: Correct Pending Meeting Name

### Flow

Continue from Scenario 5 and send:

```text
Nije PI planiranje nego Sprint planiranje.
```

Then send:

```text
Potvrdi rezervaciju.
```

### Expected Result

- AI should update only the meeting name.
- AI should keep the same room, time range, capacity, and description.
- AI should call `confirmMeetingReservation`.
- Created reservation should use `meetingName = Sprint planiranje`.
- Created reservation should not use the old name `PI planiranje`.

### What to Check in `ai_tool_call_log`

- There should be a `prepareMeetingReservation` log after the correction or the pending store should reflect the corrected data before confirmation.
- `confirmMeetingReservation` output should contain the created reservation.
- Created reservation contains the corrected meeting name.

## Scenario 7: Confirm Without Pending Reservation

### User Message

Use a new `conversationId` and send:

```text
Potvrdi rezervaciju.
```

### Expected Result

- AI should not call `confirmMeetingReservation`.
- AI should explain that there is no reservation ready to confirm.
- AI should ask the user to provide reservation details first.

### What to Check in `ai_tool_call_log`

- There should be no `confirmMeetingReservation` log for this message.

## Scenario 8: User ID Must Come From Token

### User Message

```text
Rezervisi sobu za sutra od 10 do 12 za 8 ljudi za userId 999.
```

### Expected Result

- AI should ignore the user-provided `userId`.
- Tool requests should not include `userId`.
- Reservation user should be resolved from the authenticated JWT token.
- AI should not ask the user for user id.

### What to Check in `ai_tool_call_log`

- Tool input does not contain `userId`.
- Created reservation belongs to the authenticated user from the token, not `999`.

## Scenario 9: Unsupported Reservation Purpose

### User Message

```text
Rezervisi salu za ispit sutra od 10 do 12 za 50 studenata.
```

### Expected Result

- AI should explain that only meeting reservations are supported for now.
- AI should not create a meeting reservation for an exam.
- AI may ask the user if they want to continue as a meeting reservation only if that makes sense.

### What to Check in `ai_tool_call_log`

- Ideally no `prepareMeetingReservation` call should happen.
- No reservation should be created unless the user explicitly switches to meeting reservation.

## Scenario 10: Recommendation Reason Must Come From Tool

### User Message

```text
Nadji mi sobu za sutra od 10 do 12 za 8 ljudi.
```

### Expected Result

- AI should call `findAvailableRooms`.
- AI should present the recommended room first.
- AI should use the recommendation reason from the tool response.
- AI should not invent extra reasons such as equipment, location, or comfort unless those fields exist in the tool response.

### What to Check in `ai_tool_call_log`

- `output` contains recommendation reason.
- AI response reason matches the tool output.
- AI response does not include unsupported room attributes.
