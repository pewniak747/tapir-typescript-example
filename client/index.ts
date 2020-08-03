import fetch from "isomorphic-fetch";

type Widget = Components.Schemas.Widget;

function getWidgets(): Promise<Widget[]> {
  return fetch("http://localhost:8080/widgets")
    .then<Paths.GetWidgets.Responses.$200>((r) => r.json())
    .then((r) => r.widgets || []);
}

function createWidget(body: Paths.CreateWidget.RequestBody): Promise<number> {
  return fetch("http://localhost:8080/widgets", {
    method: "POST",
    body: JSON.stringify(body),
  })
    .then<Paths.CreateWidget.Responses.$200>((r) => r.json())
    .then((r) => r.id);
}

getWidgets().then((widgets) => {
  console.log("Widgets fetched: ", widgets);
});

createWidget({ name: "ABC", description: "Test" }).then((id) => {
  console.log("Widget created: ", id);
});
