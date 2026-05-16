const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
admin.initializeApp();

exports.notifyAvailability = functions.database
    .ref('/users/{userId}/available')
    .onUpdate((change, context) => {

        const wasAvailable = change.before.val();
        const isAvailableNow = change.after.val();

        if (isAvailableNow === true && wasAvailable !== true) {

            const userId = context.params.userId;

            return admin.database().ref(`/users/${userId}`).once('value').then(snapshot => {
                const user = snapshot.val();

                const message = {
                    notification: {
                        title: "¡Usuario Disponible!",
                        body: `${user.name} ${user.lastname} acaba de conectarse y está disponible.`
                    },
                    data: {
                        trackUserId: userId
                    },
                    topic: "Available"
                };
                return admin.messaging().send(message)
                    .then((response) => {
                        console.log("Notificación enviada con éxito:", response);
                        return null;
                    })
                    .catch((error) => {
                        console.error("Error enviando notificación:", error);
                        return null;
                    });
            });
        }

        return null;
    });