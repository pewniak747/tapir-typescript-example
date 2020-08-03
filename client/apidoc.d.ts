declare namespace Components {
    namespace Schemas {
        export interface Widget {
            id: number; // int64
            name: string;
            description?: string;
        }
        export interface WidgetCreate {
            name: string;
            description?: string;
        }
        export interface WidgetCreateSuccess {
            id: number; // int64
        }
        export interface WidgetsGetSuccess {
            widgets?: Widget[];
        }
    }
}
declare namespace Paths {
    namespace CreateWidget {
        export type RequestBody = Components.Schemas.WidgetCreate;
        namespace Responses {
            export type $200 = Components.Schemas.WidgetCreateSuccess;
        }
    }
    namespace GetWidgets {
        namespace Responses {
            export type $200 = Components.Schemas.WidgetsGetSuccess;
        }
    }
}

