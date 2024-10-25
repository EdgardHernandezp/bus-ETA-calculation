package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'when the message is put in the queue successfully'
    label 'message-sent-ok'
    input {
        triggeredBy 'triggerLocationSending()'
    }
    outputMessage {
        sentTo 'bus-location-queue'
        body(
                [
                        'busNumber'  : alphaNumeric(),
                        'routeNumber': alphaNumeric(),
                        'location'   : [
                                'altitude' : value(number()),
                                'longitude': value(number())
                        ]
                ]
        )
        headers {
            messagingContentType(applicationJson())
        }
    }
}