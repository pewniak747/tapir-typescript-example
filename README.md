# Tapir, Swagger & TypeScript example

## Prerequisites

- `sbt` >= 1.3.8
- `npm` >= 6.13
- `node` >= 12.16

## Running instructions

1. Compile & run the server:

```sh
sbt server/run
```

2. Visit http://localhost:8080 to see the Swagger API docs.

3. Install client packages:

```sh
npm install
```

4. Generate the TypeScript schema file to `client/api.d.ts`:

```sh
npm run dtsgen
```

5. Build the client example:

```sh
npm run build
```

6. Run the client example:

```sh
npm run client
```

## TMWL: Generating TypeScript typings for REST APIs

Recently, I've been a part of a project which consists of a JSON REST API (implemented using Scala & [Tapir](https://github.com/softwaremill/tapir)) and a Single Page Application browser client (implemented using TypeScript).

I like the type-safety on both the server and client sides that the languages provide.
However, the communication between them is not statically typed.
Errors can still happen at runtime if e.g. we reference a non-existent field from the response JSON, or forget to provide a required field in the request JSON body.

This got me thinking...
Tapir already [generates documentation for our API](https://blog.softwaremill.com/describe-then-interpret-http-endpoints-using-tapir-ac139ba565b0), using Swagger.
This takes the form of a YAML file.
If we could generate TypeScript typings from this API documentation, we would get compilation errors whenever the client uses the API incorrectly.
Wouldn't that be great?

Luckily, there's a package called [dtsgenerator](https://www.npmjs.com/package/dtsgenerator) that can do just that!
Point it at your swagger API definition and it will spit out TypeScript types representing JSON requests & responses.
Here's an example from a publicly hosted API:

```sh
npx dtsgen --url https://petstore.swagger.io/v2/swagger.json > src/apidoc.d.ts
```

The generated `d.ts` file includes types like these:

```typescript
export interface Order {
  id: number;
  productId: number;
  quantity: number;
  shipDate: string;
  status: "placed" | "approved" | "delivered";
  complete: boolean;
}
```

and needs to be included your project's `tsconfig.json` under `"files"` property:

```json
"files": ["src/index.ts", "src/apidoc.d.ts"]
```

This solution definitely isn't 100% failure-proof.
For example, it will only verify types of JSON bodies - you can still make a mistake by requesting a wrong HTTP path or using a wrong verb (e.g. PUT instead of POST).
However, it still provides a lot of type safety, is very simple to use, and is compatible with any API that uses Swagger for documentation.

To see a complete example of a REST API implemented with Tapir and a TypeScript client, visit https://github.com/pewniak747/tapir-typescript-example
