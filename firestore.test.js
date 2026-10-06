const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const ADMIN_UID = "admin_123";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read members", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("members").get());
});

test("Unauthenticated user: cannot create members", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("members").doc("m1").set({
    id: "m1",
    fullName: "Manuel António",
    congregation: "Novo Golfe 01",
    designation: "Ancião",
    primaryPhone: "923456789",
    hasPrimaryWhatsApp: true,
    isPresent: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Authenticated user: can read and create valid member", async () => {
  const authDb = testEnv.authenticatedContext(ADMIN_UID).firestore();
  await assertSucceeds(authDb.collection("members").doc("m1").set({
    id: "m1",
    fullName: "Manuel António",
    congregation: "Novo Golfe 01",
    designation: "Ancião",
    primaryPhone: "923456789",
    hasPrimaryWhatsApp: true,
    isPresent: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }));

  await assertSucceeds(authDb.collection("members").get());
});

test("Invalid phone number or designation is rejected", async () => {
  const authDb = testEnv.authenticatedContext(ADMIN_UID).firestore();
  // Invalid phone (only 8 digits)
  await assertFails(authDb.collection("members").doc("m2").set({
    id: "m2",
    fullName: "Manuel António",
    congregation: "Novo Golfe 01",
    designation: "Ancião",
    primaryPhone: "92345678",
    hasPrimaryWhatsApp: true,
    isPresent: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }));

  // Invalid designation
  await assertFails(authDb.collection("members").doc("m3").set({
    id: "m3",
    fullName: "Manuel António",
    congregation: "Novo Golfe 01",
    designation: "Bispo",
    primaryPhone: "923456789",
    hasPrimaryWhatsApp: true,
    isPresent: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Admin Account: authenticated user can set and read admin account", async () => {
  const authDb = testEnv.authenticatedContext(ADMIN_UID).firestore();
  await assertSucceeds(authDb.collection("admin_accounts").doc("Lazaro_Luis").set({
    adminName: "Lázaro Luis",
    password: "Lázaro Luis 234",
    updatedAt: new Date()
  }));

  await assertSucceeds(authDb.collection("admin_accounts").doc("Lazaro_Luis").get());
});
